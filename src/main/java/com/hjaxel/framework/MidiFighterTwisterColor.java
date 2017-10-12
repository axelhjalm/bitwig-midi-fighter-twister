/*
 *     Bitwig Extension for Midi Fighter Twister
 *     Copyright (C) 2017 Axel Hjälm
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

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
