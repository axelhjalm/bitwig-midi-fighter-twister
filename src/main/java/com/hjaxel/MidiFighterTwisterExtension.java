package com.hjaxel;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.hjaxel.framework.Encoder;
import com.hjaxel.framework.IntSetting;
import com.hjaxel.navigation.CursorNavigator;
import com.hjaxel.page.MidiListener;
import com.hjaxel.page.device.DeviceTrack;
import com.hjaxel.page.drum.DrumSequencer;

import java.util.*;

public class MidiFighterTwisterExtension extends ControllerExtension {

    private ControllerHost host;
    private final Map<Integer, CursorNavigator> navigators = new HashMap<>();
    private final List<MidiListener> listeners = new ArrayList<>();

    protected MidiFighterTwisterExtension(final MidiFighterTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }


    @Override
    public void init() {
        host = getHost();

        mTransport = host.createTransport();
        host.getMidiInPort(0).setMidiCallback((ShortMidiMessageReceivedCallback) msg -> onMidi0(msg));
        host.getMidiInPort(0).setSysexCallback((String data) -> onSysex0(data));
        Clip clip = host.createLauncherCursorClip(16, 16);


        listeners.add(new DeviceTrack(host));
        listeners.add(new DrumSequencer(host, clip));



        host.showPopupNotification("Midi Fighter Twister Initialized");
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
        print(msg);

        listeners.forEach(l -> l.onMessage(msg));
    }

    private void print(String s) {
        host.println(s);
    }

    private void print(ShortMidiMessage msg) {
        host.println(String.format("S[%s] C[%s] D1[%s] D2[%s]", msg.getStatusByte(), msg.getChannel(), msg.getData1(), msg.getData2()));
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

    private Transport mTransport;
}
