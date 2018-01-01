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

package com.hjaxel.framework;

import com.bitwig.extension.controller.api.MidiOut;

import java.util.Optional;



public enum Encoder {

    Track(MidiChannel.CHANNEL_0, 0),

    Volume(MidiChannel.CHANNEL_0, 1),
    Mute(MidiChannel.CHANNEL_1, 1),

    Pan(MidiChannel.CHANNEL_0, 2),
    PanReset(MidiChannel.CHANNEL_1, 2),

    ParameterPageNavigation(MidiChannel.CHANNEL_0, 4),
    DeviceNavigation(MidiChannel.CHANNEL_0, 5),
    PlayHead(MidiChannel.CHANNEL_0, 3),
    Play(MidiChannel.CHANNEL_1, 3),
    PlayPulse(MidiChannel.CHANNEL_2, 3);

    private final MidiChannel channel;
    private final int cc;

    public static Optional<Encoder> from(MidiMessage msg) {
        for (Encoder encoder : Encoder.values()) {
            if (encoder.cc == msg.getCc() && encoder.channel == msg.getChannel()) {
                return Optional.of(encoder);
            }
        }

        return Optional.empty();
    }

    Encoder(MidiChannel channel, int cc) {
        this.cc = cc;
        this.channel = channel;
    }


    public void send(MidiOut outPort, int value) {
        outPort.sendMidi(channel.value(), cc, value);
    }
}
