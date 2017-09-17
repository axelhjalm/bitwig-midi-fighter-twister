package com.hjaxel.page.drum;

import com.bitwig.extension.callback.StepDataChangedCallback;
import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.PlayingNote;
import com.bitwig.extension.controller.api.PlayingNoteArrayValue;
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

    public static final int LOW_NOTE = 36;
    public static final int HIGH_NOTE = 51;
    private final AtomicReference<DrumSequencer> selected = new AtomicReference<>();
    private final Map<Integer, DrumSequencer> pads = new HashMap<>();
    private final int[] encoderPadLookup = new int[]{48, 49, 50, 51, 44, 45, 46, 47, 40, 41, 42, 43, 36, 37, 38, 39};

    private static final List<MidiChannelAndRange> observedMessages = Arrays.asList(
            MidiChannelAndRange.of(MidiChannel.CHANNEL_0, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_1, 16, 31),
            MidiChannelAndRange.of(MidiChannel.CHANNEL_3, 16, 19)
    );
    private final Clip clip;

    public DrumPad16(ControllerHost host, Clip clip) {
        super(host);
        this.clip = host.createLauncherCursorClip(127, 127);

        clip.setIsSubscribed(true);

        this.clip.playingStep().markInterested();
        this.clip.addStepDataObserver((x, y, state) -> {
            print("i got state");
        });

        this.clip.getPlayStart().markInterested();
        this.clip.getPlayStop().markInterested();
        this.clip.getLoopStart().markInterested();
        this.clip.getLoopLength().markInterested();
        this.clip.isLoopEnabled().markInterested();
        this.clip.getShuffle().markInterested();
        this.clip.getAccent().markInterested();
        this.clip.canScrollStepsBackwards().markInterested();
        this.clip.canScrollStepsForwards().markInterested();


        PlayingNoteArrayValue playingNoteArrayValue = cursorTrack().playingNotes();
        playingNoteArrayValue.markInterested();
        playingNoteArrayValue.setIsSubscribed(true);
        playingNoteArrayValue.addValueObserver(this::noteObserver);

        enableObservers(true);

        for (int i = LOW_NOTE; i <= HIGH_NOTE; i++) {
            pads.put(i, new DrumSequencer(host, clip, i));
        }
    }

    private void noteObserver(PlayingNote[] playingNotes) {
        List<Integer> playing = new ArrayList<>();
        for (PlayingNote playingNote : playingNotes) {
            playing.add(playingNote.pitch());
            sendValue(MidiChannel.CHANNEL_1, playingNote.pitch()-16, 127);
        }

        for(int i = 31; i < 51; i++){
            if (!playing.contains(i)){
                sendValue(MidiChannel.CHANNEL_1, i - 16, 80);
            }
        }
    }

    public void enableObservers(final boolean enable) {
        this.clip.playingStep().setIsSubscribed(enable);
        this.clip.getPlayStart().setIsSubscribed(enable);
        this.clip.getPlayStop().setIsSubscribed(enable);
        this.clip.getLoopStart().setIsSubscribed(enable);
        this.clip.getLoopLength().setIsSubscribed(enable);
        this.clip.isLoopEnabled().setIsSubscribed(enable);
        this.clip.getShuffle().setIsSubscribed(enable);
        this.clip.getAccent().setIsSubscribed(enable);
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
