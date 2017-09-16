package com.hjaxel;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.hjaxel.framework.Encoder;
import com.hjaxel.navigation.CursorNavigator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MidiFighterTwisterExtension extends ControllerExtension {

    private static final int NO_OF_CONTROLS = 8;
    private ControllerHost host;
    private MidiOut outPort;
    private CursorRemoteControlsPage remoteControlsPage;
    private PinnableCursorDevice cursorDevice;
    private CursorTrack cursorTrack;
    private final Map<Integer, CursorNavigator> navigators = new HashMap<>();

    protected MidiFighterTwisterExtension(final MidiFighterTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }


    @Override
    public void init() {
        host = getHost();

        mTransport = host.createTransport();
        host.getMidiInPort(0).setMidiCallback((ShortMidiMessageReceivedCallback) msg -> onMidi0(msg));
        host.getMidiInPort(0).setSysexCallback((String data) -> onSysex0(data));

        outPort = host.getMidiOutPort(0);

        cursorTrack = host.createCursorTrack("2f9fce85-6a96-46a7-b8b4-ad097ee13f9d", "cursor-track", 0, 0, true);
        cursorDevice = cursorTrack.createCursorDevice("76fad0dc-1a84-408f-8d18-66ae5f93a21f", "cursor-device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
        remoteControlsPage = cursorDevice.createCursorRemoteControlsPage(NO_OF_CONTROLS);

        addListener(Encoder.Volume, cursorTrack.getVolume());
        addListener(Encoder.Pan, cursorTrack.getPan());
        addListener(Encoder.PlayPulse, mTransport.isPlaying());

        navigators.put(0, new CursorNavigator(cursorTrack, 10));
        navigators.put(4, new CursorNavigator(remoteControlsPage, 10));
        navigators.put(5, new CursorNavigator(cursorDevice, 10));

        addParameterPageControls();

        // TODO: Perform your driver initialization here.
        // For now just show a popup notification for verification that it is running.
        host.showPopupNotification("Midi Fighter Twister Initialized");
    }

    private void addListener(Encoder encoder, SettableBooleanValue booleanValue) {
        booleanValue.addValueObserver(b -> {
            if (booleanValue.get()) {
                encoder.send(outPort, 6);
            } else {
                encoder.send(outPort, 0);
            }
        });
    }

    private void addListener(Encoder encoder, Parameter parameter) {
        parameter.value().addValueObserver(128, val -> encoder.send(outPort, val));
    }

    private void addParameterPageControls() {
        for (int i = 0; i < NO_OF_CONTROLS; i++) {
            RemoteControl parameter = remoteControlsPage.getParameter(i);
            final int x = i;
            parameter.value().addValueObserver(128, value -> outPort.sendMidi(176, resolveEncoder(x), value));
        }
    }


    private int resolveEncoder(int x) {
        return 8 + x;
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
        //print(msg);

        Optional<Encoder> optional = Encoder.from(msg);
        if (optional.isPresent()) {
            Encoder encoder = optional.get();
            if (encoder == Encoder.Play) {
                mTransport.togglePlay();
            }
        }

        if (isTrackControl(msg)) {
            handleTrackControl(msg);
        }
        if (isDeviceParameter(msg)) {
            updateParameter(msg);
        }

        if (isCursorNavigation(msg)) {
            navigators.get(msg.getData1()).onChange(msg.getData2());
        }


    }

    private void print(String s) {
        host.println(s);
    }

    private void print(ShortMidiMessage msg) {
        host.println(String.format("S[%s] C[%s] D1[%s] D2[%s]", msg.getStatusByte(), msg.getChannel(), msg.getData1(), msg.getData2()));
    }

    private void handleTrackControl(ShortMidiMessage msg) {
        int cc = msg.getData1();
        if (cc == 1) {
            if (msg.getChannel() == 1) {
                cursorTrack.getMute().toggle();
            } else {
                cursorTrack.getVolume().value().set(msg.getData2(), 128);
            }
        } else if (cc == 2) {
            if (msg.getChannel() == 1) {
                cursorTrack.getPan().reset();
            } else {
                cursorTrack.getPan().set(msg.getData2(), 128);
            }
        } else if (cc == 3) {

        }

    }

    private boolean isTrackControl(ShortMidiMessage msg) {
        int cc = msg.getData1();
        return cc >= 1 && cc <= 7;
    }

    private boolean isCursorNavigation(ShortMidiMessage msg) {
        return navigators.containsKey(msg.getData1());
    }

    private void updateParameter(ShortMidiMessage msg) {
        RemoteControl remoteControl = remoteControlsPage.getParameter(getParameterIndex(msg));

        remoteControl.set(msg.getData2(), 128);
    }

    private boolean isDeviceParameter(ShortMidiMessage msg) {
        int data1 = msg.getData1();
        return data1 >= 8 && data1 <= 15;
    }

    private int getParameterIndex(ShortMidiMessage msg) {
        return msg.getData1() - 8;
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
