package com.hjaxel.page.device;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.*;
import com.hjaxel.framework.Encoder;
import com.hjaxel.navigation.CursorNavigator;
import com.hjaxel.page.MidiListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by axel on 2017-09-16.
 */
public class DeviceTrack extends MidiListener {

    private static final int NO_OF_CONTROLS = 8;
    private final PinnableCursorDevice cursorDevice;
    private final CursorRemoteControlsPage remoteControlsPage;
    private final Map<Integer, CursorNavigator> navigators = new HashMap<>();

    public DeviceTrack(ControllerHost host) {
        super(host);

        cursorDevice = cursorTrack().createCursorDevice("76fad0dc-1a84-408f-8d18-66ae5f93a21f", "cursor-device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
        remoteControlsPage = cursorDevice.createCursorRemoteControlsPage(NO_OF_CONTROLS);

        addListener(Encoder.Volume, cursorTrack().getVolume());
        addListener(Encoder.Pan, cursorTrack().getPan());
        addListener(Encoder.PlayPulse, transport().isPlaying());

        navigators.put(0, new CursorNavigator(cursorTrack(), 10));
        navigators.put(4, new CursorNavigator(remoteControlsPage, 10));
        navigators.put(5, new CursorNavigator(cursorDevice, 10));

        addParameterPageControls();
    }

    @Override
    protected boolean accept(ShortMidiMessage msg) {
        if (msg.getData1() > 15){
            return false;
        }
        Optional<Encoder> optional = Encoder.from(msg);
        if (optional.isPresent()) {
            Encoder encoder = optional.get();
            if (encoder == Encoder.Play) {
                transport().togglePlay();
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

        return true;
    }

    private void addListener(Encoder encoder, SettableBooleanValue booleanValue) {
        booleanValue.addValueObserver(b -> {
            if (booleanValue.get()) {
                encoder.send(midiOut(), 5);
            } else {
                encoder.send(midiOut(), 0);
            }
        });
    }

    private void addListener(Encoder encoder, Parameter parameter) {
        parameter.value().addValueObserver(128, val -> encoder.send(midiOut(), val));
    }

    private void addParameterPageControls() {
        for (int i = 0; i < NO_OF_CONTROLS; i++) {
            RemoteControl parameter = remoteControlsPage.getParameter(i);
            final int x = i;
            parameter.value().addValueObserver(128, value -> midiOut().sendMidi(176, resolveEncoder(x), value));
        }
    }

    private int resolveEncoder(int x) {
        return 8 + x;
    }


    private void handleTrackControl(ShortMidiMessage msg) {
        int cc = msg.getData1();
        if (cc == 1) {
            if (msg.getChannel() == 1) {
                cursorTrack().getMute().toggle();
            } else if(msg.getChannel() == 0){
                cursorTrack().getVolume().value().set(msg.getData2(), 128);
            }
        } else if (cc == 2) {
            if (msg.getChannel() == 1) {
                cursorTrack().getPan().reset();
            } else if(msg.getChannel() == 0){
                cursorTrack().getPan().set(msg.getData2(), 128);
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
}
