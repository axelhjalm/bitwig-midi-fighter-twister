package com.hjaxel.page.drum;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.hjaxel.page.MidiListener;

/**
 * Created by axel on 2017-09-16.
 */
public class DrumSequencer extends MidiListener {

    public DrumSequencer(ControllerHost host) {
        super(host);
    }

    @Override
    protected boolean accept(ShortMidiMessage midiMessage) {
        return false;
    }
}
