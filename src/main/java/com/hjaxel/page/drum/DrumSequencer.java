package com.hjaxel.page.drum;

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
    private final int note;

    public DrumSequencer(ControllerHost host, Clip clip, int note) {
        super(host);
        this.clip = clip;
        this.note = note;
        clip.setName("Drum Sequencer");
        for (int i = 0; i < 16; i++) {
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
            int step = midiMessage.getCc() - 16;

            switch (midiMessage.getChannel()) {
                case CHANNEL_0:
                    if (activeSteps[step]) {
                        clip.setStep(step, note, midiMessage.getVelocity(), 0.25);
                        break;
                    }
                case CHANNEL_1:
                    if (midiMessage.getVelocity() == 127) {
                        activeSteps[step] = true;
                        clip.setStep(step, note, 90, 0.25);
                        sendValue(MidiChannel.CHANNEL_1, midiMessage.getCc(), 90);
                    }
                    if (midiMessage.getVelocity() == 0) {
                        activeSteps[step] = false;
                        clip.clearStep(step, note);
                        sendTurnOff(MidiChannel.CHANNEL_0, midiMessage.getCc());
                        sendTurnOff(MidiChannel.CHANNEL_1, midiMessage.getCc());
                    }
                    break;
                default:
                    break;
            }

        return true;
    }

}
