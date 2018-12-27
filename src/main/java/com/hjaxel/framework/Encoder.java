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

import com.bitwig.extension.controller.api.MidiOut;

import java.util.Optional;

public enum Encoder {

    Track(MidiChannel.CHANNEL_0, 0),
    Solo(MidiChannel.CHANNEL_1, 0),
    SendSolo(MidiChannel.CHANNEL_1, 32),

    Volume(MidiChannel.CHANNEL_0, 1),
    Mute(MidiChannel.CHANNEL_1, 1),
    SendMute(MidiChannel.CHANNEL_1, 33),

    Pan(MidiChannel.CHANNEL_0, 2),
    PanReset(MidiChannel.CHANNEL_1, 2),

    ParameterPageNavigation(MidiChannel.CHANNEL_0, 4),
    DeviceNavigation(MidiChannel.CHANNEL_0, 5),
    PlayHead(MidiChannel.CHANNEL_0, 3),
    Play(MidiChannel.CHANNEL_1, 3),
    PlayPulse(MidiChannel.CHANNEL_2, 3),
    SendPlayPulse(MidiChannel.CHANNEL_2, 35),
    DisplayRemoteControls(MidiChannel.CHANNEL_1, 4),
    DisplayDevice(MidiChannel.CHANNEL_1, 5),
    ToggleDevice(MidiChannel.CHANNEL_1, 6),
    Preset(MidiChannel.CHANNEL_0, 7),
    PresetCommit(MidiChannel.CHANNEL_1, 7),

    Parameter1(MidiChannel.CHANNEL_0, 8),
    Parameter2(MidiChannel.CHANNEL_0, 9),
    Parameter3(MidiChannel.CHANNEL_0, 10),
    Parameter4(MidiChannel.CHANNEL_0, 11),
    Parameter5(MidiChannel.CHANNEL_0, 12),
    Parameter6(MidiChannel.CHANNEL_0, 13),
    Parameter7(MidiChannel.CHANNEL_0, 14),
    Parameter8(MidiChannel.CHANNEL_0, 15),

    ParameterFine1(MidiChannel.CHANNEL_4, 8),
    ParameterFine2(MidiChannel.CHANNEL_4, 9),
    ParameterFine3(MidiChannel.CHANNEL_4, 10),
    ParameterFine4(MidiChannel.CHANNEL_4, 11),
    ParameterFine5(MidiChannel.CHANNEL_4, 12),
    ParameterFine6(MidiChannel.CHANNEL_4, 13),
    ParameterFine7(MidiChannel.CHANNEL_4, 14),
    ParameterFine8(MidiChannel.CHANNEL_4, 15),


    P2Knob1(MidiChannel.CHANNEL_0, 16),
    P2Knob2(MidiChannel.CHANNEL_0, 17),
    P2Knob3(MidiChannel.CHANNEL_0, 18),
    P2Knob4(MidiChannel.CHANNEL_0, 19),
    P2Knob5(MidiChannel.CHANNEL_0, 20),
    P2Knob6(MidiChannel.CHANNEL_0, 21),
    P2Knob7(MidiChannel.CHANNEL_0, 22),
    P2Knob8(MidiChannel.CHANNEL_0, 23),
    P2Knob9(MidiChannel.CHANNEL_0, 24),
    P2Knob10(MidiChannel.CHANNEL_0, 25),
    P2Knob11(MidiChannel.CHANNEL_0, 26),
    P2Knob12(MidiChannel.CHANNEL_0, 27),
    P2Knob13(MidiChannel.CHANNEL_0, 28),
    P2Knob14(MidiChannel.CHANNEL_0, 29),
    P2Knob15(MidiChannel.CHANNEL_0, 30),
    P2Knob16(MidiChannel.CHANNEL_0, 31),

    P2Knob1Press(MidiChannel.CHANNEL_1, 16),
    P2Knob2Press(MidiChannel.CHANNEL_1, 17),
    P2Knob3Press(MidiChannel.CHANNEL_1, 18),
    P2Knob4Press(MidiChannel.CHANNEL_1, 19),
    P2Knob5Press(MidiChannel.CHANNEL_1, 20),
    P2Knob6Press(MidiChannel.CHANNEL_1, 21),
    P2Knob7Press(MidiChannel.CHANNEL_1, 22),
    P2Knob8Press(MidiChannel.CHANNEL_1, 23),
    P2Knob9Press(MidiChannel.CHANNEL_1, 24),
    P2Knob10Press(MidiChannel.CHANNEL_1, 25),
    P2Knob11Press(MidiChannel.CHANNEL_1, 26),
    P2Knob12Press(MidiChannel.CHANNEL_1, 27),
    P2Knob13Press(MidiChannel.CHANNEL_1, 28),
    P2Knob14Press(MidiChannel.CHANNEL_1, 29),
    P2Knob15Press(MidiChannel.CHANNEL_1, 30),
    P2Knob16Press(MidiChannel.CHANNEL_1, 31),

    Send1(MidiChannel.CHANNEL_0, 40),
    Send2(MidiChannel.CHANNEL_0, 41),
    Send3(MidiChannel.CHANNEL_0, 42),
    Send4(MidiChannel.CHANNEL_0, 43),
    Send5(MidiChannel.CHANNEL_0, 44),
    Send6(MidiChannel.CHANNEL_0, 45),
    Send7(MidiChannel.CHANNEL_0, 46),
    Send8(MidiChannel.CHANNEL_0, 47),
    SendToggle1(MidiChannel.CHANNEL_1, 40),
    SendToggle2(MidiChannel.CHANNEL_1, 41),
    SendToggle3(MidiChannel.CHANNEL_1, 42),
    SendToggle4(MidiChannel.CHANNEL_1, 43),
    SendToggle5(MidiChannel.CHANNEL_1, 44),
    SendToggle6(MidiChannel.CHANNEL_1, 45),
    SendToggle7(MidiChannel.CHANNEL_1, 46),
    SendToggle8(MidiChannel.CHANNEL_1, 47),

    SendTrackScroll(MidiChannel.CHANNEL_0, 32),
    SendVolume(MidiChannel.CHANNEL_0, 33),
    SendPan(MidiChannel.CHANNEL_0, 34),
    SendPanReset(MidiChannel.CHANNEL_1, 34),
    SendScroll(MidiChannel.CHANNEL_0, 35),
    SendPlay(MidiChannel.CHANNEL_1, 35),

    LoopStart(MidiChannel.CHANNEL_0, 36),
    LoopStop(MidiChannel.CHANNEL_0, 37),
    LoopToggle1(MidiChannel.CHANNEL_1, 36),
    LoopToggle2(MidiChannel.CHANNEL_1, 37),

    Zoom(MidiChannel.CHANNEL_0, 38),
    ArrangerZoomFull(MidiChannel.CHANNEL_1, 38),

    Device(MidiChannel.CHANNEL_3, 0),
    Drums(MidiChannel.CHANNEL_3, 1),
    Mixer(MidiChannel.CHANNEL_3, 2),
    Volumes(MidiChannel.CHANNEL_3, 3),

    GotoMixer(MidiChannel.CHANNEL_3, 10),
    GotoDevice2(MidiChannel.CHANNEL_3, 26),
    GotoMixer2(MidiChannel.CHANNEL_3, 28),
    GotoVolume(MidiChannel.CHANNEL_3, 11),
    GotoVolume2(MidiChannel.CHANNEL_3, 23),
    GotoDevice(MidiChannel.CHANNEL_3, 20),

    Color(MidiChannel.CHANNEL_0, 39),

    Volume1(MidiChannel.CHANNEL_0, 48),
    Volume2(MidiChannel.CHANNEL_0, 49),
    Volume3(MidiChannel.CHANNEL_0, 50),
    Volume4(MidiChannel.CHANNEL_0, 51),
    Volume5(MidiChannel.CHANNEL_0, 52),
    Volume6(MidiChannel.CHANNEL_0, 53),
    Volume7(MidiChannel.CHANNEL_0, 54),
    Volume8(MidiChannel.CHANNEL_0, 55),
    Volume9(MidiChannel.CHANNEL_0, 56),
    Volume10(MidiChannel.CHANNEL_0, 57),
    Volume11(MidiChannel.CHANNEL_0, 58),
    Volume12(MidiChannel.CHANNEL_0, 59),
    Volume13(MidiChannel.CHANNEL_0, 60),
    Volume14(MidiChannel.CHANNEL_0, 61),
    Volume15(MidiChannel.CHANNEL_0, 62),
    Volume16(MidiChannel.CHANNEL_0, 63),

    Volume1Fine(MidiChannel.CHANNEL_1, 48),
    Volume2Fine(MidiChannel.CHANNEL_1, 49),
    Volume3Fine(MidiChannel.CHANNEL_1, 50),
    Volume4Fine(MidiChannel.CHANNEL_1, 51),
    Volume5Fine(MidiChannel.CHANNEL_1, 52),
    Volume6Fine(MidiChannel.CHANNEL_1, 53),
    Volume7Fine(MidiChannel.CHANNEL_1, 54),
    Volume8Fine(MidiChannel.CHANNEL_1, 55),
    Volume9Fine(MidiChannel.CHANNEL_1, 56),
    Volume10Fine(MidiChannel.CHANNEL_1, 57),
    Volume11Fine(MidiChannel.CHANNEL_1, 58),
    Volume12Fine(MidiChannel.CHANNEL_1, 59),
    Volume13Fine(MidiChannel.CHANNEL_1, 60),
    Volume14Fine(MidiChannel.CHANNEL_1, 61),
    Volume15Fine(MidiChannel.CHANNEL_1, 62),
    Volume16Fine(MidiChannel.CHANNEL_1, 63),

    VolumeTrackBankPrevious(MidiChannel.CHANNEL_3, 29),
    VolumeTrackBankNext(MidiChannel.CHANNEL_3, 31),
    ;

    private final MidiChannel channel;
    private final int cc;

    public MidiChannel getChannel() {
        return channel;
    }

    public int getCc() {
        return cc;
    }

    public int knob(){
        return this.cc % 16;
    }

    public static Optional<Encoder> from(MidiMessage msg) {
        for (Encoder encoder : Encoder.values()) {
            if (encoder.cc == msg.getCc() && encoder.channel == msg.getChannel()) {
                return Optional.of(encoder);
            }
        }

        return Optional.empty();
    }

    Encoder(MidiChannel channel, int cc) {
        this.cc = cc;
        this.channel = channel;
    }


    public void send(MidiOut outPort, int value) {
        outPort.sendMidi(channel.value(), cc, value);
    }
}
