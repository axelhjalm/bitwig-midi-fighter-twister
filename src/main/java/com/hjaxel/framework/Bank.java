package com.hjaxel.framework;

public enum Bank {
    BANK_1(147, 0, 127),
    BANK_2(147, 1, 127),
    BANK_3(147, 2, 127),
    BANK_4(147, 3, 127);

    private final int status;
    private final int data1;
    private final int data2;

    Bank(int status, int data1, int data2) {
        this.status = status;
        this.data1 = data1;
        this.data2 = data2;
    }

    public int getStatus() {
        return status;
    }

    public int getData1() {
        return data1;
    }

    public int getData2() {
        return data2;
    }
}
