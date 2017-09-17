package com.hjaxel.page.drum;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ClipLauncherSlotBank;
import com.bitwig.extension.controller.api.ControllerHost;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiChannelAndRange;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.page.MidiListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by axel on 2017-09-16.
 */
public class DrumSequencer extends MidiListener {

    private boolean[] activeSteps = new boolean[16];

    private static final List<MidiChannelAndRange> observedMessages = Arrays.asList(
            MidiChannelAndRange.of(MidiChannel.CHANNEL_0, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_1, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_3, 19, 19)
    );
    private final Clip clip;

    public DrumSequencer(ControllerHost host, Clip clip) {
        super(host);
        this.clip = clip;
        clip.setName("Drum Sequencer");
        for(int i = 0; i < 16; i++){
            activeSteps[i] = false;
        }
    }

    @Override
    protected boolean accept(MidiMessage midiMessage) {
        for (MidiChannelAndRange midiChannelAndRange : observedMessages) {
            if (midiChannelAndRange.accepts(midiMessage)) {
                return handle(midiMessage);
            }
        }

        return false;
    }

    private boolean handle(MidiMessage midiMessage) {

        if (isCreateClip(midiMessage)) {

            ClipLauncherSlotBank slotBank = clip.getTrack().clipLauncherSlotBank();

            slotBank.createEmptyClip(0, 16);
        } else {
            int step = midiMessage.getCc() - 16;
            int velocity = midiMessage.getVelocity();

            // toggle on
            if (midiMessage.getChannel() == MidiChannel.CHANNEL_1 && velocity == 127) {
                activeSteps[step] = true;
                clip.setStep(step, 36, 90, 0.25);
                midiOut().sendMidi(176, midiMessage.getCc(), 90);
            }
            // toggle off
            if (midiMessage.getChannel() == MidiChannel.CHANNEL_1 && velocity == 0) {
                activeSteps[step] = false;
                clip.clearStep(step, 36);
                sendTurnOff(MidiChannel.CHANNEL_0, midiMessage.getCc());
                sendTurnOff(MidiChannel.CHANNEL_1, midiMessage.getCc());
            }
            // change velocity (can it be blocked if not toggled?)
            if (midiMessage.getChannel() == MidiChannel.CHANNEL_0 && activeSteps[step]) {
                clip.setStep(step, 36, velocity, 0.25);
            }
        }

        return true;
    }

    private boolean isCreateClip(MidiMessage midiMessage) {
        return midiMessage.getChannel() == MidiChannel.CHANNEL_3 && midiMessage.getCc() == 19 && midiMessage.getVelocity() > 0;
    }
}
