package com.hjaxel.page.drum;

import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ControllerHost;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiChannelAndRange;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.page.MidiListener;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by axel on 2017-09-17.
 */
public class DrumPad16 extends MidiListener {

    private final AtomicReference<DrumSequencer> selected = new AtomicReference<>();
    private final Map<Integer, DrumSequencer> pads = new HashMap<>();
    private final int[] encoderPadLookup = new int[]{48, 49, 50, 51, 44, 45, 46, 47, 40, 41, 42, 43, 36, 37, 38, 39};

    private static final List<MidiChannelAndRange> observedMessages = Arrays.asList(
            MidiChannelAndRange.of(MidiChannel.CHANNEL_0, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_1, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_3, 16, 19)
    );

    public DrumPad16(ControllerHost host, Clip clip) {
        super(host);
        for (int i = 36; i <= 51; i++) {
            pads.put(i, new DrumSequencer(host, clip, i));
        }
    }

    @Override
    protected boolean accept(MidiMessage midiMessage) {

        for (MidiChannelAndRange midiChannelAndRange : observedMessages) {
            if (midiChannelAndRange.accepts(midiMessage)) {
                print(this.getClass().getSimpleName() + " accepted " + midiMessage);
                return handle(midiMessage);
            }
        }
        return false;
    }

    private boolean handle(MidiMessage midiMessage) {
        switch (midiMessage.getChannel()) {
            case CHANNEL_3:
                if (midiMessage.getCc() == 16) {
                    selected.set(null);
                    drawEmptyGrid();
                }
                break;
            case CHANNEL_0:
            case CHANNEL_1:
                if (isPadSelected()) {
                    selected.get().accept(midiMessage);
                } else {
                    int pad = encoderPadLookup[midiMessage.getCc() - 16];
                    DrumSequencer sequencer = pads.get(pad);
                    selected.set(sequencer);
                    sequencer.onFocus();
                }
        }
        return true;
    }

    private boolean isPadSelected() {
        return selected.get() != null;
    }

    private void drawEmptyGrid() {
        for (int i = 0; i < 16; i++) {
            sendValue(MidiChannel.CHANNEL_0, 16 + i, 0); // ring color
            sendValue(MidiChannel.CHANNEL_1, 16 + i, 80); // LED color
        }
    }

}
