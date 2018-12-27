package com.hjaxel;

import com.hjaxel.framework.Bank;

public enum TwisterMode {
    DEVICE(Bank.BANK_1),
    DRUMS(Bank.BANK_2),
    MIXER(Bank.BANK_3),
    VOLUMES(Bank.BANK_4),
    ;

    private final Bank bank;

    TwisterMode(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() { return this.bank; }
}
