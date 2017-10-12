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
public class MidiMessage {

    MidiChannel channel;
    int cc;
    int velocity;

    public MidiMessage(MidiChannel channel, int cc, int velocity) {
        this.channel = channel;
        this.cc = cc;
        this.velocity = velocity;
    }

    public MidiChannel getChannel() {
        return channel;
    }

    public int getCc() {
        return cc;
    }

    public int getVelocity() {
        return velocity;
    }

    public boolean isCCInRange(int from, int to) {
        return cc >= from && cc <= to;
    }

    @Override
    public String toString() {
        return "MidiMessage{" +
                "channel=" + channel +
                ", cc=" + cc +
                ", velocity=" + velocity +
                '}';
    }
}
