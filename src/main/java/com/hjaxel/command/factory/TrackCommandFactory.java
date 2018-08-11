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

import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.SendBank;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;
import com.hjaxel.UserSettings;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.track.*;
import com.hjaxel.framework.ColorMap;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.MidiFighterTwister;
import com.hjaxel.navigation.CursorNavigator;

import java.util.function.Consumer;

public class TrackCommandFactory {

    private final CursorNavigator trackNavigation;
    private final CursorTrack track;
    private final TrackBank trackBank;
    private final MidiFighterTwister twister;
    private final ColorMap colorMap;
    private SendBank sendBank;

    public TrackCommandFactory(CursorTrack track, TrackBank trackBank, UserSettings settings, MidiFighterTwister twister) {
        this.track = track;
        this.track.color().markInterested();
        this.trackBank = trackBank;
        this.twister = twister;
        trackNavigation = new CursorNavigator(track, settings);
        colorMap = new ColorMap();
        sendBank = track.sendBank();
    }

    public PanCommand pan(double value) {
        return new PanCommand(track, value);
    }

    public BitwigCommand volume(int trackNo, double value) {
        return () -> {
            Track item = trackBank.getItemAt(trackNo);
            item.getVolume().set(value, 128);
        };
    }

    public VolumeCommand volume(double value) {
        return new VolumeCommand(track, value);
    }

    public MuteCommand mute() {
        return new MuteCommand(track);
    }

    public BitwigCommand solo() {
        return new SoloCommand(track);
    }

    public BitwigCommand panReset() {
        return new ResetPanCommand(track);
    }

    public BitwigCommand scroll(int direction) {
        return () -> {
            trackNavigation.onChange(64 + direction);
            ColorMap.TwisterColor color = colorMap.get(track.color().red(), track.color().green(), track.color().blue());
            twister.color(Encoder.Track, color);
            twister.color(Encoder.Volume, color);
            twister.color(Encoder.Pan, color);
            twister.color(Encoder.SendTrackScroll, color);
            twister.color(Encoder.SendVolume, color);
            twister.color(Encoder.SendPan, color);
        };
    }

    public BitwigCommand send(int sendNo, int velocity, Consumer<String> c) {

        return new SendCommand(sendBank, sendNo, velocity, c);
    }

    public BitwigCommand nextSendPage(){
        return sendBank::scrollPageForwards;
    }

    public BitwigCommand previousSendPage(){
        return sendBank::scrollPageBackwards;
    }

    public BitwigCommand nextTrackPage() {
        return trackBank::scrollPageForwards;
    }

    public BitwigCommand previousTrackPage() {
        return trackBank::scrollPageBackwards;
    }

    public void color(ColorMap.TwisterColor direction) {
        track.color().set(direction.red, direction.green, direction.blue);
    }
}
