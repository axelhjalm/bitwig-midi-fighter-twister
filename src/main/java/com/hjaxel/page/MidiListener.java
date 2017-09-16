package com.hjaxel.page;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.Transport;

/**
 * Created by axel on 2017-09-16.
 */
public abstract class MidiListener {

    private final ControllerHost host;
    private final MidiOut outPort;
    private final Transport transport;

    public MidiListener(ControllerHost host) {
        this.host = host;
        this.outPort = host.getMidiOutPort(0);
        transport = host.createTransport();
    }
    protected final Transport transport() {
        return transport;
    }

    protected final MidiOut midiOut() {
        return outPort;
    }

    protected final ControllerHost host() {
        return host;
    }

    public final void onMessage(ShortMidiMessage midiMessage) {
        accept(midiMessage);
    }

    protected abstract boolean accept(ShortMidiMessage midiMessage);

}
