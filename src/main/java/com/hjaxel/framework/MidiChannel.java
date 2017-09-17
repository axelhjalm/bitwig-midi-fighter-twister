package com.hjaxel.framework;

/**
 * Created by axel on 2017-09-17.
 */
public enum MidiChannel {

    CHANNEL_0(175),
    CHANNEL_1(176),
    CHANNEL_2(177),
    CHANNEL_3(178),
    CHANNEL_4(179),
    CHANNEL_5(180),
    CHANNEL_6(181),
    CHANNEL_7(182),
    CHANNEL_8(183),
    CHANNEL_9(184),
    CHANNEL_10(185),
    CHANNEL_11(186),
    CHANNEL_12(187),
    CHANNEL_13(188),
    CHANNEL_14(189),
    CHANNEL_15(190);

    private final int value;

    public boolean is(int v){
        return value - 175 == v;
    }

    MidiChannel(int value) {
        this.value = value;
    }

    public int channel(){
        return value - 175;
    }

    public int value() {
        return value + 1;
    }

    public static MidiChannel from(int channel) {
        return MidiChannel.valueOf("CHANNEL_" + channel);
    }

    @Override
    public String toString() {
        return String.valueOf(value - 175);
    }
}
