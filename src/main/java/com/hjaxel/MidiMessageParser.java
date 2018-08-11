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
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.NoAction;
import com.hjaxel.command.application.ZoomCommand;
import com.hjaxel.command.application.ZoomToFitCommand;
import com.hjaxel.command.factory.DeviceCommandFactory;
import com.hjaxel.command.factory.TrackCommandFactory;
import com.hjaxel.command.factory.TransportCommandFactory;
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

    public MidiMessageParser(TrackCommandFactory cursorTrack, TransportCommandFactory transport, DeviceCommandFactory device,
                             UserSettings settings, Application application, MidiFighterTwister twister) {
        this.track = cursorTrack;
        this.transport = transport;
        this.device = device;
        this.settings = settings;
        this.application = application;
        this.twister = twister;
    }

    public BitwigCommand parse(MidiMessage midiMessage, Consumer<String> c) {
        return Encoder.from(midiMessage)
                .map(encoder -> toCommand(encoder, midiMessage, c)).orElse(new NoAction(midiMessage));
    }

    private BitwigCommand toCommand(Encoder encoder, MidiMessage midiMessage, Consumer<String> c) {
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
                return track.pan(midiMessage.getVelocity());
            case Volume:
            case SendVolume:
                return track.volume(midiMessage.getVelocity());
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
                return device.parameter(0, settings.coarse(), midiMessage.direction());
            case Parameter2:
                return device.parameter(1, settings.coarse(), midiMessage.direction());
            case Parameter3:
                return device.parameter(2, settings.coarse(), midiMessage.direction());
            case Parameter4:
                return device.parameter(3, settings.coarse(), midiMessage.direction());
            case Parameter5:
                return device.parameter(4, settings.coarse(), midiMessage.direction());
            case Parameter6:
                return device.parameter(5, settings.coarse(), midiMessage.direction());
            case Parameter7:
                return device.parameter(6, settings.coarse(), midiMessage.direction());
            case Parameter8:
                return device.parameter(7, settings.coarse(), midiMessage.direction());

            case ParameterFine1:
                return device.parameter(0, settings.fine(), midiMessage.direction());
            case ParameterFine2:
                return device.parameter(1, settings.fine(), midiMessage.direction());
            case ParameterFine3:
                return device.parameter(2, settings.fine(), midiMessage.direction());
            case ParameterFine4:
                return device.parameter(3, settings.fine(), midiMessage.direction());
            case ParameterFine5:
                return device.parameter(4, settings.fine(), midiMessage.direction());
            case ParameterFine6:
                return device.parameter(5, settings.fine(), midiMessage.direction());
            case ParameterFine7:
                return device.parameter(6, settings.fine(), midiMessage.direction());
            case ParameterFine8:
                return device.parameter(7, settings.fine(), midiMessage.direction());


            case SendTrackScroll:
                return track.scroll(midiMessage.direction());
            case Send1:
                return track.send(0, midiMessage.getVelocity(), c);
            case Send2:
                return track.send(1, midiMessage.getVelocity(), c);
            case Send3:
                return track.send(2, midiMessage.getVelocity(), c);
            case Send4:
                return track.send(3, midiMessage.getVelocity(), c);
            case Send5:
                return track.send(4, midiMessage.getVelocity(), c);
            case Send6:
                return track.send(5, midiMessage.getVelocity(), c);
            case Send7:
                return track.send(6, midiMessage.getVelocity(), c);
            case Send8:
                return track.send(7, midiMessage.getVelocity(), c);

            case SendToggle1:
                return track.send(0, 0, c);
            case SendToggle2:
                return track.send(1, 0, c);
            case SendToggle3:
                return track.send(2, 0, c);
            case SendToggle4:
                return track.send(3, 0, c);
            case SendToggle5:
                return track.send(4, 0, c);
            case SendToggle6:
                return track.send(5, 0, c);
            case SendToggle7:
                return track.send(6, 0, c);
            case SendToggle8:
                return track.send(7, 0, c);
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
                return () -> {};
            case Mixer:
                return () -> application.toggleMixer();

            case GotoMixer:
            case GotoMixer2:
                return () ->
                {
                    twister.selectBank3();
                    application.toggleMixer();
                };
            case GotoDevice:
            case GotoDevice2:
                return () -> {
                    twister.selectBank1();
                    application.toggleDevices();
                };
            case GotoVolume:
            case GotoVolume2:
                return () -> {
                    twister.selectBank4();
                    application.toggleMixer();
                };

            case Volume1:
                return track.volume(0, midiMessage.getVelocity());
            case Volume2:
                return track.volume(1, midiMessage.getVelocity());
            case Volume3:
                return track.volume(2, midiMessage.getVelocity());
            case Volume4:
                return track.volume(3, midiMessage.getVelocity());
            case Volume5:
                return track.volume(4, midiMessage.getVelocity());
            case Volume6:
                return track.volume(5, midiMessage.getVelocity());
            case Volume7:
                return track.volume(6, midiMessage.getVelocity());
            case Volume8:
                return track.volume(7, midiMessage.getVelocity());
            case Volume9:
                return track.volume(8, midiMessage.getVelocity());
            case Volume10:
                return track.volume(9, midiMessage.getVelocity());
            case Volume11:
                return track.volume(10, midiMessage.getVelocity());
            case Volume12:
                return track.volume(11, midiMessage.getVelocity());
            case Volume13:
                return track.volume(12, midiMessage.getVelocity());
            case Volume14:
                return track.volume(13, midiMessage.getVelocity());
            case Volume15:
                return track.volume(14, midiMessage.getVelocity());
            case Volume16:
                return track.volume(15, midiMessage.getVelocity());

            case VolumeTrackBankNext:
                return track.next();
            case VolumeTrackBankPrevious:
                return track.previous();

        }

        throw new IllegalStateException("Unhandled message " + midiMessage);
    }

}
