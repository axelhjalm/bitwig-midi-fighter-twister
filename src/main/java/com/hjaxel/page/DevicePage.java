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

import com.bitwig.extension.callback.BooleanValueChangedCallback;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.SettableColorValue;
import com.hjaxel.framework.ColorMap;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.MidiFighterTwister;

import java.util.Objects;

public class DevicePage {
    private final CursorTrack cursorTrack;
    private final MidiFighterTwister twister;
    private final MidiOut midiOut;
    private final ColorMap colorMap = new ColorMap();

    public DevicePage(CursorTrack cursorTrack, MidiFighterTwister twister, MidiOut midiOut) {
        Objects.requireNonNull(cursorTrack, "CursorTrack");
        Objects.requireNonNull(twister, "MidiFighterTwister");
        Objects.requireNonNull(midiOut, "MidiOut");
        this.cursorTrack = cursorTrack;
        this.twister = twister;
        this.midiOut = midiOut;

        cursorTrack.addIsSelectedInMixerObserver(onTrackFocus());
        cursorTrack.addIsSelectedInEditorObserver(onTrackFocus());

        SettableColorValue settableColorValue = cursorTrack.color();
        settableColorValue.markInterested();
        settableColorValue.addValueObserver((r, g, b) -> {
            ColorMap.TwisterColor color = colorMap.get(r, g, b);
            twister.color(Encoder.Track, color);
            twister.color(Encoder.Volume, color);
            twister.color(Encoder.Pan, color);
            twister.color(Encoder.SendTrackScroll, color);
            twister.color(Encoder.SendVolume, color);
            twister.color(Encoder.SendPan, color);
            twister.color(Encoder.Color, color);
        });
    }

    private BooleanValueChangedCallback onTrackFocus() {
        return trackSelected -> {
            if (trackSelected) {
                Encoder.Volume.send(midiOut, midiValue(cursorTrack.volume()));
                Encoder.Pan.send(midiOut, midiValue(cursorTrack.pan()));
            }
        };
    }


    private int midiValue(Parameter p) {
        return Math.max(0, (int) (127 * p.value().get()));
    }


}
