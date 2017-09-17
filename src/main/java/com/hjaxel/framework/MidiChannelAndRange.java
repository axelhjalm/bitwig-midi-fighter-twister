package com.hjaxel.framework;

/**
 * Created by axel on 2017-09-16.
 */
public class MidiChannelAndRange {

    private final MidiChannel channel;
    private final int lower;
    private final int upper;

    private MidiChannelAndRange(MidiChannel channel, int lower, int upper){
        this.channel = channel;
        this.lower = lower;
        this.upper = upper;
    }

    public static MidiChannelAndRange of(MidiChannel channel, int lower, int upper){
        return new MidiChannelAndRange(channel, lower, upper);
    }

    public boolean accepts(MidiMessage midiMessage){
        if (channel != midiMessage.getChannel()){
            return false;
        }

        return midiMessage.isCCInRange(lower, upper);
    }

}
