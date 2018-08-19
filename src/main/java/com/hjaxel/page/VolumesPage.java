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

package com.hjaxel.page;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.SettableColorValue;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.framework.ColorMap;
import com.hjaxel.framework.MidiFighterTwister;

import java.util.Objects;

public class VolumesPage {

    private final TrackBank trackBank;
    private MidiFighterTwister twister;
    private ColorMap colorMap = new ColorMap();

    public VolumesPage(ControllerHost host, MidiFighterTwister twister) {
        Objects.requireNonNull(host, "ControllerHost");
        Objects.requireNonNull(twister, "MidiFighterTwister");
        this.trackBank = host.createTrackBank(16, 0, 0, true);
        this.twister = twister;

        for (int x = 0; x < 16; x++) {
            int knob = knobAtPage3(x);

            Track item = trackBank.getItemAt(x);
            SettableColorValue settableColorValue = item.color();
            settableColorValue.markInterested();
            settableColorValue.addValueObserver((r, g, b) -> {
                boolean active = item.isActivated().get();
                twister.color(knob, active ? colorMap.get(r, g, b).twisterValue : 0);
            });

            item.isActivated().addValueObserver(b -> {
                if (!b) {
                    twister.color(knob, 0);
                } else {
                    twister.color(knob, colorMap.get(item.color()).twisterValue);
                    twister.value(knob, (int) (item.volume().get() * 127));
                }
            });

            item.solo().addValueObserver(isSolo -> {
                if (isSolo)
                    twister.startFlash(knob);
                else
                    twister.stopFlash(knob);
            });

            item.volume().value().addValueObserver(128, value -> {
                boolean active = item.isActivated().get();
                twister.value(knob, active ? value : 0);
            });

        }
    }

    private int knobAtPage3(int i) {
        return 48 + i;
    }

    public BitwigCommand next() {
        return trackBank::scrollPageForwards;
    }

    public BitwigCommand previous() {
        return trackBank::scrollPageBackwards;
    }

    public BitwigCommand volume(int trackNo, int delta, double scale) {
        return () -> {
            Track item = trackBank.getItemAt(trackNo);
            item.volume().inc(delta, scale);
        };
    }

    public BitwigCommand solo(int idx) {
        return () -> {
            Track item = trackBank.getItemAt(idx);
            item.solo().toggle();
        };
    }

}
