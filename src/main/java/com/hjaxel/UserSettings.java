package com.hjaxel;

import com.bitwig.extension.controller.api.RangedValue;
import com.bitwig.extension.controller.api.SettableEnumValue;
import com.bitwig.extension.controller.api.SettableRangedValue;

public class UserSettings {

    private SettableEnumValue speed;
    private SettableEnumValue navigationSpeed;

    public UserSettings(SettableEnumValue navigationSpeed, SettableEnumValue speed) {
        this.navigationSpeed = navigationSpeed;
        this.speed = speed;
    }


    public double coarse(){
        return Math.pow(2, 7);
    }

    public double fine(){
        return Math.pow(2, speed(speed));
    }

    private double speed(SettableEnumValue speed) {
        String s = speed.get();
        if("Slow".equals(s)){
            return 10;
        }
        if("Medium".equals(s)){
            return 9;
        }

        return 7;

    }

    public int getNavigationSpeed() {
        String s = navigationSpeed.get();
        if("Slow".equals(s)){
            return 15;
        }
        if("Medium".equals(s)){
            return 5;
        }

        return 2;
    }


}
