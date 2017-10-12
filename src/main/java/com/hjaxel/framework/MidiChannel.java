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
