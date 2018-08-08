/*
 *     Bitwig Extension for Midi Fighter Twister
 *     Copyright (C) 2017 Axel Hjälm
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.hjaxel.page.device;

import com.bitwig.extension.callback.BooleanValueChangedCallback;
import com.bitwig.extension.controller.api.*;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.navigation.CursorNavigator;
import com.hjaxel.page.MidiFighterTwisterControl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by axel on 2017-09-16.
 */
public class DeviceTrack extends MidiFighterTwisterControl {

    private static final int NO_OF_CONTROLS = 8;
    private final PinnableCursorDevice cursorDevice;
    private final CursorRemoteControlsPage remoteControlsPage;
    private final Map<Integer, CursorNavigator> navigators = new HashMap<>();
    private final PopupBrowser popupBrowser;
    private final SettableRangedValue coarseControl;
    private final SettableRangedValue fineControl;
    private final MidiIn midiInPort;
    private final NoteInput noteInput;

    public DeviceTrack(ControllerHost host, SettableRangedValue coarseControl, SettableRangedValue fineControl) {
        super(host, 0);
        this.coarseControl = coarseControl;
        this.fineControl = fineControl;

        cursorDevice = cursorTrack().createCursorDevice("76fad0dc-1a84-408f-8d18-66ae5f93a21f", "cursor-device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
        cursorTrack().addIsSelectedInMixerObserver(onTrackFocus());
        cursorTrack().addIsSelectedInEditorObserver(onTrackFocus());
        remoteControlsPage = cursorDevice.createCursorRemoteControlsPage(NO_OF_CONTROLS);

        popupBrowser = super.host().createPopupBrowser();


        midiInPort = host.getMidiInPort(0);
        noteInput = midiInPort.createNoteInput("program-change");


        addListener(Encoder.Volume, cursorTrack().getVolume());
        addListener(Encoder.Pan, cursorTrack().getPan());
        addListener(Encoder.PlayPulse, transport().isPlaying());

        navigators.put(0, new CursorNavigator(cursorTrack(), 10));
        navigators.put(4, new CursorNavigator(remoteControlsPage, 10));
        navigators.put(5, new CursorNavigator(cursorDevice, 10));

        addParameterPageControls();
    }

    private BooleanValueChangedCallback onTrackFocus() {
        return trackSelected -> {
            if (trackSelected) {
                Encoder.Volume.send(midiOut(), midiValue(cursorTrack().getVolume()));
                Encoder.Pan.send(midiOut(), midiValue(cursorTrack().getPan()));
            }
        };
    }

    private int midiValue(Parameter p) {
        return Math.max(0, (int) (127 * p.value().get()));
    }

    @Override
    protected boolean accept(MidiMessage msg) {
        print(msg.toString());
        if (msg.getCc() > 15 || !(msg.getChannel() == MidiChannel.CHANNEL_0 || msg.getChannel() == MidiChannel.CHANNEL_1 || msg.getChannel() == MidiChannel.CHANNEL_4)) {
            return false;
        }

        Optional<Encoder> optional = Encoder.from(msg);
        if (optional.isPresent()) {
            Encoder encoder = optional.get();
            switch (encoder) {
                case Mute:
                    cursorTrack().getMute().toggle();
                    break;
                case Solo:
                    cursorTrack().getSolo().toggle();
                    break;
                case Play:
                    transport().togglePlay();
                    break;
                case PlayHead:
                    transport().incPosition(msg.getVelocity() - 64, true);
                    break;
                case ToggleDevice:
                    cursorDevice.isEnabled().toggle();
                    break;
                case DisplayDevice:
                    cursorDevice.isWindowOpen().toggle();
                    break;
                default:
                    break;

            }
        }

        if (isTrackControl(msg)) {
            handleTrackControl(msg);
        }

        if (isDeviceParameterFine(msg)) {
            updateParameter(msg, fineControl.get() * 4196);
        }

        if (isDeviceParameter(msg)) {
            updateParameter(msg, coarseControl.get() * 512);
        }

        if (isCursorNavigation(msg)) {
            navigators.get(msg.getCc()).onChange(msg.getVelocity());
        }

        if (isPreset(msg)) {
            cursorDevice.browseToReplaceDevice();
            if (msg.getVelocity() == 65) {
                popupBrowser.selectNextFile();
            } else if (msg.getVelocity() == 63) {
                popupBrowser.selectPreviousFile();
            }
        }

        if (isCommit(msg)) {
            popupBrowser.commit();
        }

        return true;
    }

    private boolean isDeviceParameterFine(MidiMessage msg) {
        return msg.isCCInRange(8, 15) && msg.getChannel() == MidiChannel.CHANNEL_4;
    }

    private boolean isPreset(MidiMessage msg) {
        return msg.getChannel() == MidiChannel.CHANNEL_0 && msg.getCc() == 7;
    }

    private boolean isCommit(MidiMessage msg) {
        return msg.getChannel() == MidiChannel.CHANNEL_1 && msg.getCc() == 7;
    }

    private void addListener(Encoder encoder, SettableBooleanValue booleanValue) {
        booleanValue.addValueObserver(b -> {
            if (booleanValue.get()) {
                encoder.send(midiOut(), 6);
            } else {
                encoder.send(midiOut(), 0);
            }
        });
    }

    private void addListener(Encoder encoder, Parameter parameter) {
        parameter.value().addValueObserver(128, val -> encoder.send(midiOut(), val));
    }

    private void addParameterPageControls() {
        for (int i = 0; i < NO_OF_CONTROLS; i++) {
            RemoteControl parameter = remoteControlsPage.getParameter(i);

            final int x = i;
            parameter.value().addValueObserver(128, value -> midiOut().sendMidi(176, resolveEncoder(x), value));
            parameter.value().addValueObserver(128, value -> midiOut().sendMidi(180, resolveEncoder(x), value));
        }
    }

    private int resolveEncoder(int x) {
        return 8 + x;
    }


    private void handleTrackControl(MidiMessage msg) {
        if (msg.getCc() == 1) {
            if (msg.getChannel() == MidiChannel.CHANNEL_0) {
                cursorTrack().getVolume().value().set(msg.getVelocity(), 128);
            }
        } else if (msg.getCc() == 2) {
            if (msg.getChannel() == MidiChannel.CHANNEL_1) {
                cursorTrack().getPan().reset();
            } else if (msg.getChannel() == MidiChannel.CHANNEL_0) {
                cursorTrack().getPan().set(msg.getVelocity(), 128);
            }
        }
    }

    private boolean isTrackControl(MidiMessage msg) {
        return msg.isCCInRange(1, 7);
    }

    private boolean isCursorNavigation(MidiMessage msg) {
        return navigators.containsKey(msg.getCc());
    }

    private void updateParameter(MidiMessage msg, double scale) {
        RemoteControl remoteControl = remoteControlsPage.getParameter(getParameterIndex(msg));
        double v = scale * remoteControl.get();
        int direction = msg.getVelocity() == 63 ? -2 : 2;
        double value = Math.max(0, Math.min(scale - 1, direction + v));
        remoteControl.set(value, scale);
    }

    private boolean isDeviceParameter(MidiMessage msg) {
        return msg.isCCInRange(8, 15) && msg.getChannel() == MidiChannel.CHANNEL_0;
    }

    private int getParameterIndex(MidiMessage msg) {
        return msg.getCc() - 8;
    }
}
