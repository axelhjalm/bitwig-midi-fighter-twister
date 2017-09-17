package com.hjaxel.page.drum;

import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ControllerHost;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.page.MidiListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by axel on 2017-09-17.
 */
public class DrumPad16 extends MidiListener {

    private final AtomicReference<DrumSequencer> selected = new AtomicReference<>();
    private final Map<Integer, DrumSequencer> pads = new HashMap<>();
    private final int[] encoderPadLookup = new int[]{48, 49, 50, 52, 44, 45, 46, 47, 40, 41, 42, 43, 36, 37, 38, 39};

    public DrumPad16(ControllerHost host, Clip clip) {
        super(host);
        for (int i = 36; i < 51; i++) {
            pads.put(i, new DrumSequencer(host, clip, i));
        }
    }

    @Override
    protected boolean accept(MidiMessage midiMessage) {
        print(midiMessage.toString());
        switch (midiMessage.getChannel()) {
            case CHANNEL_3:
                if (midiMessage.getCc() == 16){
                    selected.set(null);
                }
                break;
            case CHANNEL_1:
                if (isPadSelected()) {
                    selected.get().accept(midiMessage);
                } else {
                    int pad = encoderPadLookup[midiMessage.getCc() - 16];
                    selected.set(pads.get(pad));
                }
        }
        return true;
    }

    private boolean isPadSelected() {
        return selected.get() != null;
    }


}
