package com.hjaxel.framework;

import com.bitwig.extension.controller.api.SoloValue;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Tracks {

    private final TrackBank trackBank;
    private Consumer<String> log;
    private final MidiFighterTwister twister;
    private int page = 0;
    private final ColorMap colorMap;

    public Tracks(TrackBank bank, MidiFighterTwister twister, Consumer<String> log) {
        this.trackBank = bank;
        this.trackBank.setChannelScrollStepSize(16);
        trackBank.canScrollBackwards().markInterested();
        this.log = log;
        trackBank.channelCount().markInterested();
        this.twister = twister;
        this.colorMap = new ColorMap();
    }

    public void next() {
        int next = trackBank.cursorIndex().get();
        List<Integer> items = listOfActiveTracks();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) == next && items.get(i) < trackBank.getSizeOfBank()) {
                next = items.get(i + 1);
                break;
            }
        }

        next = Math.max(0, next);
        next = Math.min(128, next);
        trackBank.cursorIndex().set(next);
    }

    public void previous() {
        int next = trackBank.cursorIndex().get();
        List<Integer> items = listOfActiveTracks();
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i) == next && items.get(i) > 0) {
                next = items.get(i - 1);
                break;
            }
        }

        next = Math.max(0, next);
        next = Math.min(128, next);
        trackBank.cursorIndex().set(next);
    }

    private Map<Integer, Integer> trackToKnob() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < trackBank.getSizeOfBank(); i++) {
            Track item = trackBank.getItemAt(i);
            if (item.isActivated().get()) {
                if (!item.trackType().get().equalsIgnoreCase("Master")) {
                    result.put(i, i);
                }
            }
        }
        return result;
    }

    private List<Integer> listOfActiveTracks() {
        List<Integer> items = new ArrayList<>();

        for (int i = 0; i < trackBank.getSizeOfBank(); i++) {
            Track item = trackBank.getItemAt(i);
            if (item.isActivated().get()) {
                if (!item.trackType().get().equalsIgnoreCase("Master")) {
                    items.add(i);
                }
            }
        }

        return items;
    }

    public void nextPage() {
        int size = trackBank.getSizeOfBank();
        if (this.page < (1 + size / 16)) {
            this.page++;
            this.flush();
        }
    }

    public void previousPage() {
        if (this.page > 0) {
            this.page--;
            this.flush();
        }
    }

    public Track get(int trackNo) {
        List<Integer> items = listOfActiveTracks();
        return trackBank.getItemAt(items.get(trackNo + (page * 16)));
    }

    public void flush() {
/*
        int offset = 16 * page;
        List<Integer> items = listOfActiveTracks();
        for (int i = offset; i < offset + 16; i++) {
            int cc = 48 + (i % 16);
            if (i < items.size()) {
                Track item = trackBank.getItemAt(items.get(i));
                SoloValue solo = item.solo();
                if (solo.get()) {
                    twister.startFlash(cc);
                } else {
                    twister.stopFlash(cc);
                }
                twister.color(cc, colorMap.get(item.color()).twisterValue);
                twister.value(cc, (int) (127 * item.volume().get()));
            } else {
                twister.color(cc, 127);
                twister.value(cc, 0);
            }
        }*/
    }

    public void flush(int trackNo) {
        List<Integer> items = listOfActiveTracks();

        if (!items.contains(trackNo)) {
            return;
        }

        int cc = 48 + (trackNo % 16);
        Track item = trackBank.getItemAt(items.indexOf(trackNo));
        SoloValue solo = item.solo();
        if (solo.get()) {
            twister.startFlash(cc);
        } else {
            twister.stopFlash(cc);
        }
        twister.color(cc, colorMap.get(item.color()).twisterValue);
        twister.value(cc, (int) (127 * item.volume().get()));
    }

    public void firstPage() {
        this.page = 0;
        flush();
    }
}
