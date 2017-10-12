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
 * Created by axel on 2017-09-16.
 */
public class MidiChannelAndRange {

    private final MidiChannel channel;
    private final int lower;
    private final int upper;

    private MidiChannelAndRange(MidiChannel channel, int lower, int upper){
        this.channel = channel;
        this.lower = lower;
        this.upper = upper;
    }

    public static MidiChannelAndRange of(MidiChannel channel, int lower, int upper){
        return new MidiChannelAndRange(channel, lower, upper);
    }

    public boolean accepts(MidiMessage midiMessage){
        if (channel != midiMessage.getChannel()){
            return false;
        }

        return midiMessage.isCCInRange(lower, upper);
    }

}
