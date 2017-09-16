package com.hjaxel.framework;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.MidiOut;

import java.util.Optional;

/**
 * Created by axel on 2017-09-16.
 */
public enum Encoder {

    Track(1, 0),
    Volume(1, 1),
    Mute(2, 1),
    Pan(1, 2),
    PanReset(2, 2),
    ParameterPageNavigation(1, 4),
    DeviceNavigation(1, 5),
    Play(1, 3),
    PlayPulse(2, 3);

    private final int channel;
    private final int cc;

    public static Optional<Encoder> from(ShortMidiMessage msg) {
        for (Encoder encoder : Encoder.values()) {
            if (encoder.cc == msg.getData1() && encoder.channel == msg.getChannel()) {
                return Optional.of(encoder);
            }
        }

        return Optional.empty();
    }

    Encoder(int channel, int cc) {
        this.cc = cc;
        this.channel = channel;
    }

    int out() {
        return 176 + channel;
    }

    public void send(MidiOut outPort, int value) {
        outPort.sendMidi(out(), cc, value);
    }
}
