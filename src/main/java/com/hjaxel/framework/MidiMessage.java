package com.hjaxel.framework;

/**
 * Created by axel on 2017-09-17.
 */
public class MidiMessage {

    MidiChannel channel;
    int cc;
    int velocity;

    public MidiMessage(MidiChannel channel, int cc, int velocity) {
        this.channel = channel;
        this.cc = cc;
        this.velocity = velocity;
    }

    public MidiChannel getChannel() {
        return channel;
    }

    public int getCc() {
        return cc;
    }

    public int getVelocity() {
        return velocity;
    }

    public boolean isCCInRange(int from, int to) {
        return cc >= from && cc <= to;
    }

    @Override
    public String toString() {
        return "MidiMessage{" +
                "channel=" + channel +
                ", cc=" + cc +
                ", velocity=" + velocity +
                '}';
    }
}
