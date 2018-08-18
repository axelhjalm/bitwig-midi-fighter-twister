package com.hjaxel.framework;

import com.bitwig.extension.controller.api.SoloValue;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;

import java.util.ArrayList;
import java.util.List;

public class Tracks {

    private final TrackBank trackBank;
    private final MidiFighterTwister twister;
    private int page = 0;

    public Tracks(TrackBank bank, MidiFighterTwister twister) {
        this.trackBank = bank;
        this.twister = twister;
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
        next = Math.min(127, next);
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
        next = Math.min(127, next);
        trackBank.cursorIndex().set(next);
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
        }
    }

    public void previousPage() {
        if (this.page > 0)
            this.page--;
    }

    public Track get(int trackNo) {
        List<Integer> items = listOfActiveTracks();
        return trackBank.getItemAt(items.get(trackNo + page * 16));
    }

    public void flush() {
        ColorMap colorMap = new ColorMap();
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
                twister.color(cc, colorMap.get(item.color().red(), item.color().green(), item.color().blue()).twisterValue);
            }
        }
    }
}
