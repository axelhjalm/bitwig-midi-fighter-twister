package com.hjaxel;

import com.bitwig.extension.controller.api.RangedValue;
import com.bitwig.extension.controller.api.SettableRangedValue;

public class UserSettings {

    private final SettableRangedValue fineControl;
    private final SettableRangedValue coarseControl;
    private RangedValue navigationSpeed;

    public UserSettings(SettableRangedValue navigationSpeed, SettableRangedValue fineControl, SettableRangedValue coarseControl) {
        this.navigationSpeed = navigationSpeed;
        this.fineControl = fineControl;
        this.coarseControl = coarseControl;
    }

    public double coarse(){
        return Math.pow(2, coarseControl.getRaw());
    }

    public double fine(){
        return Math.pow(2, fineControl.getRaw());
    }

    public int getNavigationSpeed() {
        return (int) navigationSpeed.getRaw();
    }
}
