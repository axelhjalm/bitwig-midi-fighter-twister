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

package com.hjaxel.command.device;

import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.RemoteControl;
import com.hjaxel.command.BitwigCommand;

public class ChangeParameterCommand implements BitwigCommand {
    private final RemoteControl remoteControl;
    private CursorDevice cursorDevice;
    private final double scale;
    private final int delta;

    public ChangeParameterCommand(RemoteControl remoteControl, CursorDevice cursorDevice, double scale, int delta) {
        this.remoteControl = remoteControl;
        this.cursorDevice = cursorDevice;
        this.scale = scale;
        this.delta = delta;

    }

    @Override
    public void execute() {
        cursorDevice.isRemoteControlsSectionVisible().set(true);
        double v = scale * remoteControl.get();
        int direction = delta == -1 ? -2 : 1;
        double value = Math.max(0, Math.min(scale-1, direction + v));
        remoteControl.set(value, scale);
    }

    @Override
    public String toString() {
        return "Parameter " + remoteControl.name().get() +", delta " + delta;
    }
}
