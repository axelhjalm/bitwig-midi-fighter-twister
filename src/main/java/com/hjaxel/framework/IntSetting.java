package com.hjaxel.framework;

/**
 * Created by axel on 2017-09-16.
 */
public class IntSetting {
    private volatile int value;

    public void observe(double v) {
        this.value = (int) v;
    }

    public int getValue() {
        return value;
    }
}
