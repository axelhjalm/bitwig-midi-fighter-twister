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

import com.bitwig.extension.controller.api.MidiOut;

import java.util.Objects;

public class MidiFighterTwister {

    private final MidiOut midiOut;

    public MidiFighterTwister(MidiOut midiOut) {
        this.midiOut = midiOut;
    }

    public void selectBank1() {
        midiOut.sendMidi(147, 0, 127);
    }

    public void selectBank2() {
        midiOut.sendMidi(147, 1, 127);
    }

    public void selectBank3() {
        midiOut.sendMidi(147, 2, 127);
    }

    public void selectBank4() {
        midiOut.sendMidi(147, 3, 127);
    }


    public void startFlash(int cc) {
        midiOut.sendMidi(MidiChannel.CHANNEL_2.value(), cc, 6);
    }

    public void stopFlash(int cc) {
        midiOut.sendMidi(MidiChannel.CHANNEL_2.value(), cc, 0);
    }

    public void color(int cc, int val) {
        if (val < 0 || val > 127 || cc < 0 || cc > 127) {
            throw new IllegalArgumentException("Color for cc=" + cc + ", value=" + val + " illegal");
        }
        midiOut.sendMidi(177, cc, val);
    }

    public void color(Encoder e, ColorMap.TwisterColor c) {
        midiOut.sendMidi(177, e.getCc(), c.twisterValue);
    }

    public void value(int cc, int value) {
        midiOut.sendMidi(176, cc, value); // normal mode
        midiOut.sendMidi(180, cc, value); // default mode
    }
}
