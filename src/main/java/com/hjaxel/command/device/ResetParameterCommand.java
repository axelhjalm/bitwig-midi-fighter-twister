package com.hjaxel.command.device;

import com.bitwig.extension.controller.api.RemoteControl;
import com.hjaxel.command.BitwigCommand;

public class ResetParameterCommand implements BitwigCommand {
    private final RemoteControl remoteControl;

    public ResetParameterCommand(RemoteControl remoteControl) {
        this.remoteControl = remoteControl;
    }

    @Override
    public void execute() {
        remoteControl.reset();
    }
}
