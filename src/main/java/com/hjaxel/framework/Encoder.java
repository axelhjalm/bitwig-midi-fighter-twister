package com.hjaxel.framework;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiOut;

import java.util.Optional;

/**
 * Created by axel on 2017-09-16.
 */
public enum Encoder {

    Track(MidiChannel.CHANNEL_0, 0),

    Volume(MidiChannel.CHANNEL_0, 1),
    Mute(MidiChannel.CHANNEL_1, 1),

    Pan(MidiChannel.CHANNEL_0, 2),
    PanReset(MidiChannel.CHANNEL_1, 2),

    ParameterPageNavigation(MidiChannel.CHANNEL_0, 4),
    DeviceNavigation(MidiChannel.CHANNEL_0, 5),
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
