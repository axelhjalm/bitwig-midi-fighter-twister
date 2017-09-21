package com.hjaxel.page.drum;

import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ControllerHost;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiFighterTwisterColor;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.page.MidiFighterTwisterControl;

/**
 * Created by axel on 2017-09-16.
 */
public class DrumSequencer extends MidiFighterTwisterControl {

    public static final int DEFAULT_VELOCITY = 90;
    private boolean[] activeSteps = new boolean[16];
    private int[] velocities = new int[16];

    private final Clip clip;
    private final int note;

    public DrumSequencer(ControllerHost host, Clip clip, int note) {
        super(host, 16);
        this.clip = clip;
        this.note = note;
        for (int i = 0; i < 16; i++) {
            activeSteps[i] = false;
        }
    }

    @Override
    protected boolean accept(MidiMessage midiMessage) {
        return handle(midiMessage);
    }

    private boolean handle(MidiMessage midiMessage) {
        int encoder = getEncoder(midiMessage);

        switch (midiMessage.getChannel()) {
            case CHANNEL_0:
                if (activeSteps[encoder]) {
                    velocities[encoder] = midiMessage.getVelocity();
                    clip.setStep(encoder, note, velocities[encoder], 0.25);
                }
                break;
            case CHANNEL_1:
                activeSteps[encoder] = !activeSteps[encoder];
                if (activeSteps[encoder]) {
                    clip.setStep(encoder, note, DEFAULT_VELOCITY, 0.25);
                    velocities[encoder] = DEFAULT_VELOCITY;
                    ring(encoder, velocities[encoder]);
                    led(encoder, MidiFighterTwisterColor.ACTIVE_PAD);
                } else {
                    clip.clearStep(encoder, note);
                    velocities[encoder] = 0;
                    ringOff(encoder);
                    led(encoder, MidiFighterTwisterColor.ENCODER_DEFAULT);
                }
                break;

            default:
                break;
        }


        return true;
    }

    public void onFocus() {
        drawGrid();
    }

    private void drawGrid() {
        for (int i = 0; i < 16; i++) {
            if (activeSteps[i]) {
                sendValue(MidiChannel.CHANNEL_0, 16 + i, velocities[i]); // rotary
                sendValue(MidiChannel.CHANNEL_1, 16 + i, 90); // led
            } else {
                sendValue(MidiChannel.CHANNEL_0, 16 + i, 0);
                sendValue(MidiChannel.CHANNEL_1, 16 + i, 127);
            }
        }

    }

    public void clear() {
        for (int i = 0; i < 16; i++) {
            activeSteps[i] = false;
            velocities[i] = 0;
        }
    }

    public boolean hasActiveCells() {
        for (int i = 0; i < 16; i++) {
            if (activeSteps[i]) {
                return true;
            }
        }

        return false;
    }
}
