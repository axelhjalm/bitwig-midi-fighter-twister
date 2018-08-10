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

package com.hjaxel;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.BooleanValueChangedCallback;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.factory.DeviceCommandFactory;
import com.hjaxel.command.factory.TrackCommandFactory;
import com.hjaxel.command.factory.TransportCommandFactory;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiFighterTwister;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.page.MidiFighterTwisterControl;
import com.hjaxel.page.drum.DrumPad16;

import java.util.*;
import java.util.function.Consumer;

public class MidiFighterTwisterExtension extends ControllerExtension {

    private ControllerHost host;
    private Transport mTransport;
//    private final Map<Integer, CursorNavigator> navigators = new HashMap<>();
    private final List<MidiFighterTwisterControl> listeners = new ArrayList<>();
    private SettableEnumValue debugLogging;
    private MidiMessageParser midiMessageParser;
    private CursorTrack cursorTrack;
    private MidiOut midiOut;
    private CursorRemoteControlsPage remoteControlsPage;
    private PinnableCursorDevice device;
    private Mixer mixer;

    protected MidiFighterTwisterExtension(final MidiFighterTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }


    @Override
    public void init() {
        host = getHost();
        setupMidiChannels();

        mixer = host.createMixer();

        UserSettings settings = createSettings(host.getPreferences());
        debugLogging = host.getPreferences().getEnumSetting("Debug Logging", "Debug", new String[]{"False", "True"}, "False");

        mTransport = host.createTransport();

        //listeners.add(new DeviceTrack(host, coarseControl, fineControl, cursorSpeed));
        listeners.add(new DrumPad16(host));
        listeners.add(new SideButtonConsumer(host, 0));

        Transport transport = host.createTransport();
        transport.isArrangerLoopEnabled().markInterested();
        createCursorTrack();
        addListeners(transport);


        device = cursorTrack.createCursorDevice("76fad0dc-1a84-408f-8d18-66ae5f93a21f", "cursor-device", 8, CursorDeviceFollowMode.FOLLOW_SELECTION);
        TrackCommandFactory trackFactory = new TrackCommandFactory(cursorTrack, settings);
        TransportCommandFactory transportFactory = new TransportCommandFactory(transport);
        remoteControlsPage = device.createCursorRemoteControlsPage(8);
        DeviceCommandFactory deviceFactory = new DeviceCommandFactory(remoteControlsPage, device, host.createPopupBrowser(), settings);

        addParameterPageControls();
        addSendObservers();

        MidiFighterTwister twister = new MidiFighterTwister(midiOut);
        midiMessageParser = new MidiMessageParser(trackFactory, transportFactory,
                deviceFactory, settings, host.createApplication(), twister);

        host.showPopupNotification("Midi Fighter Twister Initialized");
    }



    private void createCursorTrack() {
        cursorTrack = host.createCursorTrack("track001", "cursor-track", 8, 0, true);
        cursorTrack.addIsSelectedInMixerObserver(onTrackFocus());
        cursorTrack.addIsSelectedInEditorObserver(onTrackFocus());
    }

    private void setupMidiChannels() {
        midiOut = host.getMidiOutPort(0);
        host.getMidiInPort(0).setMidiCallback((ShortMidiMessageReceivedCallback) msg -> onMidi0(msg));
        host.getMidiInPort(0).setSysexCallback((String data) -> onSysex0(data));
    }

    private UserSettings createSettings(Preferences preferences) {
        SettableRangedValue coarseControl = preferences.getNumberSetting("Coarse Control Scale", "Parameter", 1, 12, 1, "", 7);
        SettableRangedValue fineControl = preferences.getNumberSetting("Fine Control Scale", "Parameter", 1, 12, 1, "", 9);
        SettableRangedValue cursorSpeed = preferences.getNumberSetting("Cursor Scroll Speed", "Navigation", 5, 40, 1, "", 10);
        return new UserSettings(fineControl, coarseControl, cursorSpeed);
    }

    private void addListeners(Transport transport) {
        addListener(Encoder.Volume, cursorTrack.getVolume());
        addListener(Encoder.SendVolume, cursorTrack.getVolume());
        addListener(Encoder.Pan, cursorTrack.getPan());
        addListener(Encoder.SendPan, cursorTrack.getPan());
        addListener(Encoder.PlayPulse, transport.isPlaying());
        addListener(Encoder.SendPlayPulse, transport.isPlaying());
    }


    private void addListener(Encoder encoder, SettableBooleanValue booleanValue) {
        booleanValue.addValueObserver(b -> {
            if (booleanValue.get()) {
                encoder.send(midiOut, 6);
            } else {
                encoder.send(midiOut, 0);
            }
        });
    }
    private void addListener(Encoder encoder, Parameter parameter) {
        parameter.value().addValueObserver(128, val -> encoder.send(midiOut, val));
    }


    private BooleanValueChangedCallback onTrackFocus() {
        return trackSelected -> {
            if (trackSelected) {
                Encoder.Volume.send(midiOut, midiValue(cursorTrack.getVolume()));
                Encoder.Pan.send(midiOut, midiValue(cursorTrack.getPan()));
            }
        };
    }

    private int midiValue(Parameter p) {
        return Math.max(0, (int) (127 * p.value().get()));
    }


    @Override
    public void exit() {
        // TODO: Perform any cleanup once the driver exits
        // For now just show a popup notification for verification that it is no longer running.
        getHost().showPopupNotification("Midi Fighter Twister Exited");
    }

    @Override
    public void flush() {
        // TODO Send any updates you need here.
    }

    /**
     * Called when we receive short MIDI message on port 0.
     */
    private void onMidi0(ShortMidiMessage msg) {
        MidiMessage midiMessage = new MidiMessage(MidiChannel.from(msg.getChannel()), msg.getData1(), msg.getData2());
        BitwigCommand command = midiMessageParser.parse(midiMessage ,s -> print(s));
        print(midiMessage + " == " + command.toString());
        command.execute();
        listeners.stream().map(l -> l.onMessage(msg)).reduce((a, b1) -> a || b1).ifPresent(logUnhandledMessage(msg));
    }

    private Consumer<Boolean> logUnhandledMessage(ShortMidiMessage msg) {
        return b -> {
            if (!b) {
                print(String.format("Message not handled c:%s, cc:%s, v:%s", msg.getChannel(), msg.getData1(), msg.getData2()));
            }
        };
    }

    private int resolveEncoder(int x) {
        return 8 + x;
    }


    private void addSendObservers() {
        SendBank sendBank = cursorTrack.sendBank();
        sendBank.getItemAt(0).value().addValueObserver(128, value -> midiOut.sendMidi(176, 34, value));
        sendBank.getItemAt(1).value().addValueObserver(128, value -> midiOut.sendMidi(176, 38, value));
        sendBank.getItemAt(2).value().addValueObserver(128, value -> midiOut.sendMidi(176, 42, value));
        sendBank.getItemAt(3).value().addValueObserver(128, value -> midiOut.sendMidi(176, 46, value));
        sendBank.getItemAt(4).value().addValueObserver(128, value -> midiOut.sendMidi(176, 35, value));
        sendBank.getItemAt(5).value().addValueObserver(128, value -> midiOut.sendMidi(176, 39, value));
        sendBank.getItemAt(6).value().addValueObserver(128, value -> midiOut.sendMidi(176, 43, value));
        sendBank.getItemAt(7).value().addValueObserver(128, value -> midiOut.sendMidi(176, 47, value));
    }

    private void addParameterPageControls() {
        for (int i = 0; i < 8; i++) {
            RemoteControl parameter = remoteControlsPage.getParameter(i);
            parameter.name().markInterested();
            final int x = i;
            parameter.value().addValueObserver(128, value -> midiOut.sendMidi(176, resolveEncoder(x), value));
            parameter.value().addValueObserver(128, value -> midiOut.sendMidi(180, resolveEncoder(x), value));
        }
    }

    private void print(String s) {
        boolean isDebug = Boolean.parseBoolean(debugLogging.get());
        if (isDebug) {
            host.println(s);
        }
    }

    private void print(ShortMidiMessage msg) {
        boolean isDebug = Boolean.parseBoolean(debugLogging.get());
        if (isDebug) {
            host.println(String.format("S[%s] C[%s] D1[%s] D2[%s]", msg.getStatusByte(), msg.getChannel(), msg.getData1(), msg.getData2()));
        }
    }

    /**
     * Called when we receive sysex MIDI message on port 0.
     */
    private void onSysex0(final String data) {
        // MMC Transport Controls:
        if (data.equals("f07f7f0605f7"))
            mTransport.rewind();
        else if (data.equals("f07f7f0604f7"))
            mTransport.fastForward();
        else if (data.equals("f07f7f0601f7"))
            mTransport.stop();
        else if (data.equals("f07f7f0602f7"))
            mTransport.play();
        else if (data.equals("f07f7f0606f7"))
            mTransport.record();
    }



    private class SideButtonConsumer extends MidiFighterTwisterControl {

        public SideButtonConsumer(ControllerHost host, int firstEncoder) {
            super(host, firstEncoder);
        }

        @Override
        protected boolean accept(MidiMessage midiMessage) {
            return midiMessage.getChannel() == MidiChannel.CHANNEL_3;
        }
    }
}
