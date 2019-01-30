package com.hjaxel.command.device;

import com.bitwig.extension.controller.api.CursorDevice;
import com.hjaxel.command.BitwigCommand;

public class ToggleRemoteControlsCommand implements BitwigCommand {

    private final CursorDevice device;

    public ToggleRemoteControlsCommand(CursorDevice device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.isRemoteControlsSectionVisible().toggle();
    }
}
