package com.hjaxel.framework;

/**
 * Created by Axel on 2017-09-19.
 */
public enum MidiFighterTwisterColor {

    ACTIVE_PAD(114),
    INACTIVE_PAD(80), ENCODER_DEFAULT(127);

    private int value;

    MidiFighterTwisterColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
