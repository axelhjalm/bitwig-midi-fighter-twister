package com.hjaxel.framework;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;

/**
 * Created by axel on 2017-09-16.
 */
public class MidiChannelAndRange {

    private final int channel;
    private final int lower;
    private final int upper;

    private MidiChannelAndRange(int channel, int lower, int upper){
        this.channel = channel;
        this.lower = lower;
        this.upper = upper;
    }

    public static MidiChannelAndRange of(int channel, int lower, int upper){
        return new MidiChannelAndRange(channel, lower, upper);
    }

    public boolean accepts(ShortMidiMessage midiMessage){
        if (channel != midiMessage.getChannel()){
            return false;
        }

        return upper >= midiMessage.getData1() && lower <= midiMessage.getData1();
    }

}
