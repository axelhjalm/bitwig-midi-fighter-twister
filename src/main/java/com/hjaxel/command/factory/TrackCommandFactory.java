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

import com.bitwig.extension.controller.api.*;
import com.hjaxel.UserSettings;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.track.*;
import com.hjaxel.framework.ColorMap;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.MidiFighterTwister;
import com.hjaxel.framework.Tracks;
import com.hjaxel.navigation.CursorNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TrackCommandFactory {

    private final CursorTrack track;
    private final TrackBank trackBank;
    private final Debounce debounce;
    private final Tracks tracks;
    private final Tracks volumes;
    private MidiFighterTwister twister;
    private final ColorMap colorMap;


    public TrackCommandFactory(CursorTrack track, TrackBank trackBank, MidiFighterTwister twister, Tracks volumes) {
        this.track = track;
        this.volumes = volumes;
        this.track.color().markInterested();
        this.trackBank = trackBank;
        this.twister = twister;
        colorMap = new ColorMap();

        this.tracks = new Tracks(trackBank, twister);
        this.debounce = new Debounce();
    }

    static class Debounce {
        private final int n = 5;
        private final AtomicInteger c = new AtomicInteger(0);

        boolean ok() {
            return c.getAndIncrement() % n == 0;
        }

    }


    public BitwigCommand volume(int trackNo, int delta, double scale) {
        return () -> {
            //Track item = trackBank.getItemAt(trackNo);
            Track item = volumes.get(trackNo);
            item.volume().inc(delta, scale);
        };
    }

    public BitwigCommand volume(int direction, double scale) {
        return () -> track.volume().inc(direction, scale);
    }

    public BitwigCommand pan(int direction, double scale) {
        return () -> track.pan().inc(direction, scale);
    }

    public MuteCommand mute() {
        return new MuteCommand(track);
    }

    public BitwigCommand solo(int idx) {
        return () -> {
            Track item = volumes.get(idx);
            item.solo().toggle();
        };
    }

    public BitwigCommand solo() {
        return new SoloCommand(track);
    }

    public BitwigCommand panReset() {
        return new ResetPanCommand(track);
    }

    public BitwigCommand scroll(int direction) {
        return () -> {
            if (!debounce.ok()) {
                return;
            }

            if (direction > 0) {
                tracks.next();
            } else if (direction < 0) {
                tracks.previous();
            }


            ColorMap.TwisterColor twisterColor = colorMap.get(track.color().red(), track.color().green(), track.color().blue());
            twister.color(Encoder.Track, twisterColor);
            twister.color(Encoder.Volume, twisterColor);
            twister.color(Encoder.Pan, twisterColor);
            twister.color(Encoder.SendTrackScroll, twisterColor);
            twister.color(Encoder.SendVolume, twisterColor);
            twister.color(Encoder.SendPan, twisterColor);
        };
    }


    public BitwigCommand send(int sendNo, int velocity, Consumer<String> c) {
        return new SendCommand(track, sendNo, velocity, c);
    }

    public BitwigCommand next() {
        return volumes::nextPage;
    }

    public BitwigCommand previous() {
        return volumes::previousPage;
    }

    public void color(int direction) {
        SettableColorValue color = track.color();
        ColorMap.TwisterColor twisterColor = colorMap.get(color.red(), color.green(), color.blue());
        ColorMap.TwisterColor newColor = colorMap.get(twisterColor.twisterValue + direction);
        track.color().set(newColor.red, newColor.green, newColor.blue);
    }

}
