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

package com.hjaxel.command.factory;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;
import com.hjaxel.command.BitwigCommand;

public class VolumesPage {

    private final TrackBank trackBank;

    public VolumesPage(ControllerHost host){

        this.trackBank = host.createTrackBank(16, 0, 0, true);
    }

    public VolumesPage(TrackBank trackBank) {
        this.trackBank = trackBank;
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
