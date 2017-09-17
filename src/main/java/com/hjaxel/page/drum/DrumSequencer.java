package com.hjaxel.page.drum;

import com.bitwig.extension.controller.api.Clip;
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

    public static final int DEFAULT_VELOCITY = 90;
    private boolean[] activeSteps = new boolean[16];
    private int[] velocities = new int[16];


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
      return handle(midiMessage);
    }

    private boolean handle(MidiMessage midiMessage) {
        int step = midiMessage.getCc() - 16;

        switch (midiMessage.getChannel()) {
            case CHANNEL_0:
                if (activeSteps[step]) {
                    clip.setStep(step, note, midiMessage.getVelocity(), 0.25);
                    velocities[step] = midiMessage.getVelocity();
                    break;
                }
            case CHANNEL_1:
                activeSteps[step] = !activeSteps[step];

                if (activeSteps[step]) {
                    clip.setStep(step, note, DEFAULT_VELOCITY, 0.25);
                    velocities[step] = DEFAULT_VELOCITY;
                } else {
                    clip.clearStep(step, note);
                    velocities[step] = 0;

                }
                break;

            default:
                break;
        }

        drawGrid();

        return true;
    }

    public void onFocus() {
        drawGrid();
    }

    private void drawGrid() {
        for (int i = 0; i < 15; i++) {
            if (activeSteps[i]) {
                sendValue(MidiChannel.CHANNEL_0, 16 + i, velocities[i]); // rotary
                sendTurnOn(MidiChannel.CHANNEL_1, 16 + i); // led
            } else {
                sendTurnOff(MidiChannel.CHANNEL_0, 16 + i);
                sendTurnOff(MidiChannel.CHANNEL_1, 16 + i);
            }
        }

    }
}
