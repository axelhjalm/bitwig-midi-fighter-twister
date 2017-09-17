package com.hjaxel.page.drum;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.StepDataChangedCallback;
import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ClipLauncherSlotBank;
import com.bitwig.extension.controller.api.ControllerHost;
import com.hjaxel.framework.MidiChannelAndRange;
import com.hjaxel.page.MidiListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by axel on 2017-09-16.
 */
public class DrumSequencer extends MidiListener {



    private static final List<MidiChannelAndRange> observedMessages = Arrays.asList(
            MidiChannelAndRange.of(0, 16, 31),
            MidiChannelAndRange.of(1, 16, 31),
            MidiChannelAndRange.of(3, 19, 19)
    );
    private final Clip clip;

    public DrumSequencer(ControllerHost host, Clip clip) {
        super(host);
        this.clip = clip;
        clip.setName("Drum Sequencer");

    }


    @Override
    protected boolean accept(ShortMidiMessage midiMessage) {
        for (MidiChannelAndRange midiChannelAndRange : observedMessages) {
            if (midiChannelAndRange.accepts(midiMessage)) {
                return handle(midiMessage);
            }
        }

        return false;
    }

    private boolean handle(ShortMidiMessage midiMessage) {

        if (isCreateClip(midiMessage)) {

            ClipLauncherSlotBank slotBank = clip.getTrack().clipLauncherSlotBank();

            slotBank.createEmptyClip(0, 16);
        } else {
            int beat = midiMessage.getData1() - 16;
            int vel = midiMessage.getData2();

            // toggle on
            if (midiMessage.getChannel() == 1 && midiMessage.getData2() == 127) {
                clip.setStep(beat, 36, 90, 0.25);
                midiOut().sendMidi(176, midiMessage.getData1(), 90);
            }
            // toggle off
            if (midiMessage.getChannel() == 1 && midiMessage.getData2() == 0) {
                clip.clearStep(beat, 36);
                midiOut().sendMidi(176, midiMessage.getData1(), 0);
            }
            // change velocity (can it be blocked if not toggled?)
            if (midiMessage.getChannel() == 0) {
                clip.setStep(beat, 36, vel, 0.25);
            }
        }

        return true;
    }

    private boolean isCreateClip(ShortMidiMessage midiMessage) {
        return midiMessage.getChannel() == 3 && midiMessage.getData1() == 19 && midiMessage.getData2() > 0;
    }
}
