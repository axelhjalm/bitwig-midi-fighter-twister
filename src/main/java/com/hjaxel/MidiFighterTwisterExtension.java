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

package com.hjaxel;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.factory.DeviceCommandFactory;
import com.hjaxel.command.factory.TrackCommandFactory;
import com.hjaxel.command.factory.TransportCommandFactory;
import com.hjaxel.framework.*;
import com.hjaxel.page.DevicePage;
import com.hjaxel.page.MixerPage;
import com.hjaxel.page.VolumesPage;

public class MidiFighterTwisterExtension extends ControllerExtension {

    private ControllerHost host;
    private Transport mTransport;
    private SettableEnumValue debugLogging;
    private MidiMessageParser midiMessageParser;
    private CursorTrack cursorTrack;
    private MidiOut midiOut;
    private CursorRemoteControlsPage remoteControlsPage;
    private MidiFighterTwister twister;
    private ColorMap colorMap;
    private UserControlBank userControls;
    private UserSettings settings;

    protected MidiFighterTwisterExtension(final MidiFighterTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }

    @Override
    public void init() {
        host = getHost();

        setupMidiChannels();
        setupDrumSequencer();
        createUserControl();

        colorMap = new ColorMap();
        settings = createSettings(host.getPreferences());
        debugLogging = host.getPreferences().getEnumSetting("Debug Logging", "Debug", new String[]{"False", "True"}, "False");

        mTransport = host.createTransport();

        twister = new MidiFighterTwister(midiOut);
        cursorTrack = host.createCursorTrack("track001", "cursor-track", 8, 0, true);
        new DevicePage(cursorTrack, twister, midiOut);

        Transport transport = host.createTransport();
        transport.isArrangerLoopEnabled().markInterested();
        addListeners(transport);

        TrackBank trackBank = host.createTrackBank(128, 0, 0, true);
        new MixerPage(trackBank, cursorTrack);

        PinnableCursorDevice device = cursorTrack.createCursorDevice("76fad0dc-1a84-408f-8d18-66ae5f93a21f", "cursor-device", 8, CursorDeviceFollowMode.FOLLOW_SELECTION);
        TrackCommandFactory trackFactory = new TrackCommandFactory(cursorTrack, twister, settings, new Tracks(trackBank, twister, this::print));

        VolumesPage volumesPage = new VolumesPage(host, twister);

        TransportCommandFactory transportFactory = new TransportCommandFactory(transport);
        remoteControlsPage = device.createCursorRemoteControlsPage(8);

        DeviceCommandFactory deviceFactory = new DeviceCommandFactory(remoteControlsPage, device, host.createPopupBrowser(), settings);

        addParameterPageControls();
        addSendObservers();
        setIndications();

        midiMessageParser = new MidiMessageParser(trackFactory, transportFactory, deviceFactory, settings, host.createApplication(), twister, volumesPage, userControls);
        host.showPopupNotification("Midi Fighter Twister Initialized");
    }

    private void setupDrumSequencer() {
        NoteInput drumSequencer = host.getMidiInPort(0).createNoteInput("MFT Drum Sequencer");
        drumSequencer.setShouldConsumeEvents(false);
    }

    private void createUserControl() {
        userControls = host.createUserControls(32);

        for(int i = 0; i < 16; i++){
            final int x = i;
            Parameter parameter = userControls.getControl(i);
            parameter.value().addValueObserver(128, val -> {
                twister.value(16 + x, val);
            });
        }
        for(int i = 16; i < 32; i++){
            final int x = i;
            Parameter parameter = userControls.getControl(i);
            parameter.value().addValueObserver(128, val -> {
                twister.color(x, val );
            });
        }
    }


    private void setupMidiChannels() {
        midiOut = host.getMidiOutPort(0);
        MidiIn midiIn = host.getMidiInPort(0);
        midiIn.setMidiCallback((ShortMidiMessageReceivedCallback) this::onMidi0);
        midiIn.setSysexCallback(this::onSysex0);
    }

    private UserSettings createSettings(Preferences preferences) {
        SettableEnumValue speed = preferences.getEnumSetting("Knob speed", "Settings", new String[]{"Slow", "Medium", "Fast"}, "Medium");
        SettableEnumValue navSpeed = preferences.getEnumSetting("Scroll speed", "Settings", new String[]{"Slow", "Medium", "Fast"}, "Medium");
        SettableEnumValue playFlash  = preferences.getEnumSetting("Flash on play", "Settings", new String[]{"On", "Off"}, "On");
        return new UserSettings(navSpeed, speed, playFlash);
    }

    private void addListeners(Transport transport) {
        addListener(Encoder.Volume, cursorTrack.volume());
        addListener(Encoder.SendVolume, cursorTrack.volume());
        addListener(Encoder.Pan, cursorTrack.pan());
        addListener(Encoder.SendPan, cursorTrack.pan());
        addListener(Encoder.PlayPulse, transport.isPlaying());
        addListener(Encoder.SendPlayPulse, transport.isPlaying());
    }


    private void addListener(Encoder encoder, SettableBooleanValue booleanValue) {
        booleanValue.addValueObserver(b -> {
            if (booleanValue.get() && settings.flashOnPlay() ) {
                encoder.send(midiOut, 6);
            } else {
                encoder.send(midiOut, 0);
            }
        });
    }

    private void addListener(Encoder encoder, Parameter parameter) {
        parameter.value().addValueObserver(128, val -> encoder.send(midiOut, val));
    }


    @Override
    public void exit() {
        // TODO: Perform any cleanup once the driver exits
        // For now just show a popup notification for verification that it is no longer running.
        getHost().showPopupNotification("Midi Fighter Twister Exited");
    }

    @Override
    public void flush() {
        // TODO Send any updates you need here.
    }

    /**
     * Called when we receive short MIDI message on port 0.
     */
    private void onMidi0(ShortMidiMessage msg) {
        MidiMessage midiMessage = new MidiMessage(MidiChannel.from(msg.getChannel()), msg.getData1(), msg.getData2());
        BitwigCommand command = midiMessageParser.parse(midiMessage, s -> print(s));
        print(midiMessage + " == " + command.toString());
        command.execute();
    }

    private int resolveEncoder(int x) {
        return 8 + x;
    }

    private void addSendObservers() {
        SendBank sendBank = cursorTrack.sendBank();
        for (int i = 0; i < 8; i++) {
            Send send = sendBank.getItemAt(i);
            final int x = i;
            send.sendChannelColor().addValueObserver((r, g, b) -> {
                twister.color(40 + x, colorMap.get(r, g, b).twisterValue);
            });
            send.value().addValueObserver(128, value -> midiOut.sendMidi(176, 40 + x, value));
        }
    }

    private void addParameterPageControls() {
        for (int i = 0; i < remoteControlsPage.getParameterCount(); i++) {
            RemoteControl parameter = remoteControlsPage.getParameter(i);
            parameter.name().markInterested();
            final int x = i;
            parameter.value().addValueObserver(128, value -> midiOut.sendMidi(176, resolveEncoder(x), value));
            parameter.value().addValueObserver(128, value -> midiOut.sendMidi(180, resolveEncoder(x), value));
        }
    }

    private void print(String s) {
        boolean isDebug = Boolean.parseBoolean(debugLogging.get());
        if (isDebug) {
            host.println(s);
        }
    }

    /**
     * Called when we receive sysex MIDI message on port 0.
     */
    private void onSysex0(final String data) {
        // MMC Transport Controls:
        if (data.equals("f07f7f0605f7"))
            mTransport.rewind();
        else if (data.equals("f07f7f0604f7"))
            mTransport.fastForward();
        else if (data.equals("f07f7f0601f7"))
            mTransport.stop();
        else if (data.equals("f07f7f0602f7"))
            mTransport.play();
        else if (data.equals("f07f7f0606f7"))
            mTransport.record();
    }

    private void setIndications() {
        for (int i = 0; i < remoteControlsPage.getParameterCount(); i++) {
            RemoteControl parameter = remoteControlsPage.getParameter(i);
            parameter.setIndication(true);
        }
    }
}
