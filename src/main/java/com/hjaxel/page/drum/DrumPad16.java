/*
 *     Bitwig Extension for Midi Fighter Twister
 *     Copyright (C) 2017 Axel Hjälm
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.hjaxel.page.drum;

import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.PlayingNote;
import com.bitwig.extension.controller.api.PlayingNoteArrayValue;
import com.hjaxel.UserSettings;
import com.hjaxel.framework.MidiChannel;
import com.hjaxel.framework.MidiChannelAndRange;
import com.hjaxel.framework.MidiFighterTwisterColor;
import com.hjaxel.framework.MidiMessage;
import com.hjaxel.page.MidiFighterTwisterControl;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by axel on 2017-09-17.
 */
public class DrumPad16 extends MidiFighterTwisterControl {

    public static final int LOW_NOTE = 36;
    public static final int HIGH_NOTE = 51;
    private final AtomicReference<DrumSequencer> selected = new AtomicReference<>();
    private final Map<Integer, DrumSequencer> pads = new HashMap<>();
    private final int[] encoderPadLookup = new int[]{48, 49, 50, 51, 44, 45, 46, 47, 40, 41, 42, 43, 36, 37, 38, 39};
    private final Map<Integer, Integer> noteToEncoder = new HashMap<>();
    private final AtomicBoolean overviewMode = new AtomicBoolean(true);


    private static final List<MidiChannelAndRange> observedMessages = Arrays.asList(
            MidiChannelAndRange.of(MidiChannel.CHANNEL_0, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_1, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_3, 16, 19)
    );
    private Clip clip;
    private UserSettings settings;


    private boolean isActive() {
        return this.settings.isPage2DrumMode();
    }

    public DrumPad16(ControllerHost host, UserSettings settings) {
        super(host, 16);
        this.settings = settings;

        if (isActive()) {
            populateNoteToEncoderMap();
            this.clip = host.createLauncherCursorClip(127, 127);


            this.clip.addStepDataObserver((x, y, state) -> {
                //TODO: record data into twister
            });

            subscribeToClipEvents();
            addNoteObserver();
            enableObservers(true);

            addPadListeners(host);

        }

    }

    private void addPadListeners(ControllerHost host) {
        if (isActive()) {
            for (int i = LOW_NOTE; i <= HIGH_NOTE; i++) {
                pads.put(i, new DrumSequencer(host, clip, i));
            }
        }
    }

    private void addNoteObserver() {
        if (isActive()) {
            PlayingNoteArrayValue playingNoteArrayValue = clip.getTrack().playingNotes();
            playingNoteArrayValue.markInterested();
            playingNoteArrayValue.setIsSubscribed(true);
            playingNoteArrayValue.addValueObserver(this::noteObserver);
        }
    }

    private void subscribeToClipEvents() {
        if (isActive()) {
            this.clip.setIsSubscribed(true);
            this.clip.playingStep().markInterested();
            this.clip.getPlayStart().markInterested();
            this.clip.getPlayStop().markInterested();
            this.clip.getLoopStart().markInterested();
            this.clip.getLoopLength().markInterested();
            this.clip.isLoopEnabled().markInterested();
            this.clip.getShuffle().markInterested();
            this.clip.getAccent().markInterested();
            this.clip.canScrollStepsBackwards().markInterested();
            this.clip.canScrollStepsForwards().markInterested();
        }
    }

    private void populateNoteToEncoderMap() {
        if (isActive()) {
            for (int i = 0; i < 16; i++) {
                noteToEncoder.put(encoderPadLookup[i], i + 16);
            }
        }
    }

    private void drawOverview() {
        if(isActive()){
        for (int i = 0; i < 16; i++) {
            ringOff(i);
        }
/*
            int key = LOW_NOTE + i;
            if (pads.get(key).hasActiveCells()) {
                led(key, MidiFighterTwisterColor.ACTIVE_PAD);
            } else {
                led(key, MidiFighterTwisterColor.INACTIVE_PAD);
            }
            */
        }

    }

    private void noteObserver(PlayingNote[] playingNotes) {
        if(isActive()) {
            if (overviewMode.get()) {
                List<Integer> playing = new ArrayList<>();
                for (PlayingNote playingNote : playingNotes) {
                    Integer cc = noteToEncoder.get(playingNote.pitch());
                    if (cc == null) {
                        continue;
                    }
                    playing.add(cc);
                    sendValue(MidiChannel.CHANNEL_1, cc, MidiFighterTwisterColor.ACTIVE_PAD.getValue());
                }

                for (int i = 16; i < 32; i++) {
                    if (!playing.contains(i)) {
                        sendValue(MidiChannel.CHANNEL_1, i, MidiFighterTwisterColor.INACTIVE_PAD.getValue());
                    }
                }
            }
        }
    }

    public void enableObservers(final boolean enable) {
        if(isActive()) {
            this.clip.playingStep().setIsSubscribed(enable);
            this.clip.getPlayStart().setIsSubscribed(enable);
            this.clip.getPlayStop().setIsSubscribed(enable);
            this.clip.getLoopStart().setIsSubscribed(enable);
            this.clip.getLoopLength().setIsSubscribed(enable);
            this.clip.isLoopEnabled().setIsSubscribed(enable);
            this.clip.getShuffle().setIsSubscribed(enable);
            this.clip.getAccent().setIsSubscribed(enable);
        }
    }

    @Override
    protected boolean accept(MidiMessage midiMessage) {
        if(isActive()) {
            for (MidiChannelAndRange midiChannelAndRange : observedMessages) {
                if (midiChannelAndRange.accepts(midiMessage)) {
                    return handle(midiMessage);
                }
            }
        }
        return false;
    }

    private boolean handle(MidiMessage midiMessage) {

        if (!settings.isPage2DrumMode()) {
            return true;
        }

        switch (midiMessage.getChannel()) {
            case CHANNEL_3:
                if (midiMessage.getCc() == 16) {
                    selected.set(null);
                    overviewMode.set(true);
                    drawOverview();
                }
                if (midiMessage.getCc() == 19) {
                    selected.set(null);
                    overviewMode.set(true);
                    pads.values().forEach(DrumSequencer::clear);
                    clip.clearSteps();
                    drawOverview();
                }
                break;
            case CHANNEL_0:
            case CHANNEL_1:
                if (isPadSelected()) {
                    selected.get().accept(midiMessage);
                } else {
                    if (midiMessage.getChannel() == MidiChannel.CHANNEL_1) {
                        int pad = encoderPadLookup[midiMessage.getCc() - 16];
                        DrumSequencer sequencer = pads.get(pad);
                        selected.set(sequencer);
                        overviewMode.set(false);
                        sequencer.onFocus();
                    }
                }
        }
        return true;
    }

    private boolean isPadSelected() {
        return selected.get() != null;
    }


}
