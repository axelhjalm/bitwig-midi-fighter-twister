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

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.Transport;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiFighterTwisterColor;
import com.hjaxel.framework.MidiMessage;

/**
 * Created by axel on 2017-09-16.
 */
public abstract class MidiFighterTwisterControl {

    private final ControllerHost host;
    private final MidiOut outPort;
    private final Transport transport;
    private final CursorTrack cursorTrack;
    private int firstEncoder;

    public MidiFighterTwisterControl(ControllerHost host, int firstEncoder) {
        this.host = host;
        this.outPort = host.getMidiOutPort(0);
        transport = host.createTransport();
        cursorTrack = host.createCursorTrack("2f9fce85-6a96-46a7-b8b4-ad097ee13f9d", "cursor-track", 0, 0, true);
        this.firstEncoder = firstEncoder;
    }

    protected final int encoder(int encoder){
        return firstEncoder + encoder;
    }

    protected final int getEncoder(MidiMessage msg){
        return msg.getCc() - firstEncoder;
    }

    protected final void print(ShortMidiMessage msg) {
        host.println(String.format("S[%s] C[%s] D1[%s] D2[%s]", msg.getStatusByte(), msg.getChannel(), msg.getData1(), msg.getData2()));
    }

    protected final void sendTurnOn(MidiChannel channel, int cc){
        midiOut().sendMidi(channel.value(), cc, 127);
    }

    protected final void sendTurnOff(MidiChannel channel, int cc){
        midiOut().sendMidi(channel.value(), cc, 0);
    }

    protected final void ringOff(int encoder){
        ring(encoder, 0);
    }

    protected final void ring(int encoder, int value){
        if(value < 0 || value > 127){
            print(String.format("Invalid message not sent. cc: %s, v: %s", encoder, value));
            return;
        }
        midiOut().sendMidi(MidiChannel.CHANNEL_0.value(), encoder(encoder), value);
    }

    protected final void led(int encoder, MidiFighterTwisterColor color){
        midiOut().sendMidi(MidiChannel.CHANNEL_1.value(), encoder(encoder), color.getValue());
    }

    protected final void sendValue(MidiChannel channel, int cc, int value){
        if(value < 0 || value > 127){
            print(String.format("Invalid message not sent. c: %s, cc: %s, v: %s", channel, cc, value));
            return;
        }
        midiOut().sendMidi(channel.value(), cc, value);
    }

    protected final void print(String s){
        host().println(s);
    }

    protected CursorTrack cursorTrack() {
        return cursorTrack;
    }

    protected final Transport transport() {
        return transport;
    }

    protected final MidiOut midiOut() {
        return outPort;
    }

    protected final ControllerHost host() {
        return host;
    }

    public final boolean onMessage(ShortMidiMessage midiMessage) {
        return accept(new MidiMessage(MidiChannel.from(midiMessage.getChannel()), midiMessage.getData1(), midiMessage.getData2()));
    }

    protected abstract boolean accept(MidiMessage midiMessage);

}
