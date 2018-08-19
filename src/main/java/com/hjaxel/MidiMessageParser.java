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

import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.Clip;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.NoAction;
import com.hjaxel.command.application.ZoomCommand;
import com.hjaxel.command.application.ZoomToFitCommand;
import com.hjaxel.command.factory.DeviceCommandFactory;
import com.hjaxel.command.factory.TrackCommandFactory;
import com.hjaxel.command.factory.TransportCommandFactory;
import com.hjaxel.page.VolumesPage;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.MidiFighterTwister;
import com.hjaxel.framework.MidiMessage;

import java.util.function.Consumer;

public class MidiMessageParser {

    private final TrackCommandFactory track;
    private final TransportCommandFactory transport;
    private final DeviceCommandFactory device;
    private final UserSettings settings;
    private Application application;
    private final MidiFighterTwister twister;
    private final VolumesPage volumesPage;

    public MidiMessageParser(TrackCommandFactory cursorTrack, TransportCommandFactory transport, DeviceCommandFactory device,
                             UserSettings settings, Application application, MidiFighterTwister twister, VolumesPage volumesPage) {
        this.track = cursorTrack;
        this.transport = transport;
        this.device = device;
        this.settings = settings;
        this.application = application;
        this.twister = twister;
        this.volumesPage = volumesPage;
    }

    public BitwigCommand parse(MidiMessage midiMessage, Consumer<String> c) {
        return Encoder.from(midiMessage)
                .map(encoder -> toCommand(encoder, midiMessage, c)).orElse(new NoAction(midiMessage));
    }

    private BitwigCommand toCommand(Encoder encoder, MidiMessage midiMessage, Consumer<String> log) {
        switch (encoder) {
            // track
            case Track:
                return track.scroll(midiMessage.direction());
            case Mute:
            case SendMute:
                return track.mute();
            case Solo:
            case SendSolo:
                return track.solo();
            case Pan:
            case SendPan:
                return track.pan(midiMessage.direction(), settings.fine());
            case Volume:
            case SendVolume:
                return track.volume(midiMessage.direction(), settings.fine());
            case PanReset:
            case SendPanReset:
                return track.panReset();

            // transport
            case Play:
            case SendPlay:
                return transport.play();
            case SendScroll:
            case PlayHead:
                return transport.playHeadCommand(midiMessage.direction());


            case Color:
                return () -> {
                    track.color(midiMessage.direction());
                };

            // device
            case DeviceNavigation:
                return device.scrollDevice(midiMessage.direction());
            case ParameterPageNavigation:
                return device.scrollParameterPage(midiMessage.direction());
            case DisplayDevice:
                return device.toggleDisplayDeviceCommand();
            case ToggleDevice:
                return device.toggleDeviceCommand();
            case Preset:
                return device.scrollPresetsCommand(midiMessage.direction());
            case PresetCommit:
                return device.selectPresetsCommand();
            case Parameter1:
            case Parameter2:
            case Parameter3:
            case Parameter4:
            case Parameter5:
            case Parameter6:
            case Parameter7:
            case Parameter8:
                return device.parameter(encoder.knob() - 8, settings.coarse(), midiMessage.direction(), log);

            case ParameterFine1:
            case ParameterFine2:
            case ParameterFine3:
            case ParameterFine4:
            case ParameterFine5:
            case ParameterFine6:
            case ParameterFine7:
            case ParameterFine8:
                return device.parameter(encoder.knob() - 8, settings.fine(), midiMessage.direction(), log);


            case SendTrackScroll:
                return track.scroll(midiMessage.direction());
            case Send1:
            case Send2:
            case Send3:
            case Send4:
            case Send5:
            case Send6:
            case Send7:
            case Send8:
                return track.send(encoder.knob() - 8, midiMessage.getVelocity(), log);

            case SendToggle1:
            case SendToggle2:
            case SendToggle3:
            case SendToggle4:
            case SendToggle5:
            case SendToggle6:
            case SendToggle7:
            case SendToggle8:
                return track.send(encoder.knob()-8, 0, log);

            case ArrangerZoomFull:
                return new ZoomToFitCommand(application);
            case Zoom:
                return new ZoomCommand(application, midiMessage.direction());

            // Loop Control
            case LoopStart:
                return transport.loopStart(midiMessage.direction());
            case LoopStop:
                return transport.loopEnd(midiMessage.direction());
            case LoopToggle1:
            case LoopToggle2:
                return transport.loopToggle();

            // Function toggles
            case Device:
                return () -> application.toggleDevices();
            case Drums:
                return () -> {
                };
            case Mixer:
                return () -> application.toggleMixer();

            case GotoMixer:
            case GotoMixer2:
                return twister::selectBank3;
            case GotoDevice:
            case GotoDevice2:
                return twister::selectBank1;
            case GotoVolume:
            case GotoVolume2:
                return twister::selectBank4 ;

            case Volume1:
            case Volume2:
            case Volume3:
            case Volume4:
            case Volume5:
            case Volume6:
            case Volume7:
            case Volume8:
            case Volume9:
            case Volume10:
            case Volume11:
            case Volume12:
            case Volume13:
            case Volume14:
            case Volume15:
            case Volume16:
                return volumesPage.volume(encoder.knob(), midiMessage.direction(), settings.fine());

            case Volume1Fine:
            case Volume2Fine:
            case Volume3Fine:
            case Volume4Fine:
            case Volume5Fine:
            case Volume6Fine:
            case Volume7Fine:
            case Volume8Fine:
            case Volume9Fine:
            case Volume10Fine:
            case Volume11Fine:
            case Volume12Fine:
            case Volume13Fine:
            case Volume14Fine:
            case Volume15Fine:
            case Volume16Fine:
                return volumesPage.solo(encoder.knob());

            case VolumeTrackBankNext:
                return volumesPage.next();
            case VolumeTrackBankPrevious:
                return volumesPage.previous();

        }

        throw new IllegalStateException("Unhandled message " + midiMessage);
    }

}