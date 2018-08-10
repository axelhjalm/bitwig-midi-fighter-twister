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
import com.hjaxel.UserSettings;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.track.*;
import com.hjaxel.navigation.CursorNavigator;

import java.util.function.Consumer;

public class TrackCommandFactory {

    private final CursorNavigator trackNavigation;
    private CursorTrack track;

    public TrackCommandFactory(CursorTrack track, UserSettings settings) {
        this.track = track;
        trackNavigation = new CursorNavigator(track, settings);
    }

    public PanCommand pan(double value){
        return new PanCommand(track, value);
    }

    public VolumeCommand volume(double value){
        return new VolumeCommand(track, value);
    }

    public MuteCommand mute(){
        return new MuteCommand(track);
    }

    public BitwigCommand solo() {
        return new SoloCommand(track);
    }

    public BitwigCommand panReset() {
        return new ResetPanCommand(track);
    }

    public BitwigCommand scroll(int direction) {
        return () -> trackNavigation.onChange(64 + direction);
    }

    public BitwigCommand send(int sendNo, int velocity, Consumer<String> c) {
        return new SendCommand(track, sendNo, velocity, c);
    }
}
