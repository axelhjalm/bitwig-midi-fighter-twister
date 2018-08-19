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

import com.bitwig.extension.controller.api.*;
import com.hjaxel.framework.MidiFighterTwister;

import java.util.Objects;

public class MixerPage {

    private final TrackBank trackBank;

    public MixerPage(TrackBank trackBank, CursorTrack cursorTrack) {
        Objects.requireNonNull(trackBank, "TrackBank");
        Objects.requireNonNull(cursorTrack, "CursorTrack");

        this.trackBank = trackBank;

        trackBank.followCursorTrack(cursorTrack);
        trackBank.cursorIndex().markInterested();
        setupMixerPage();
    }

    private void setupMixerPage() {
        for (int i = 0; i < trackBank.getSizeOfBank(); i++) {
            setupChannel(i);
        }
    }

    private void setupChannel(int index) {
        Track item = trackBank.getItemAt(index);
        SettableColorValue settableColorValue = item.color();
        settableColorValue.markInterested();

        item.isActivated().markInterested();
        item.isGroup().markInterested();
        item.trackType().markInterested();
        item.solo().markInterested();
    }

}
