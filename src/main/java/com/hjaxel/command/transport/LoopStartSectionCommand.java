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

package com.hjaxel.command.transport;

import com.bitwig.extension.controller.api.Transport;
import com.hjaxel.command.BitwigCommand;

public class LoopStartSectionCommand implements BitwigCommand {

    private final Transport transport;
    private final double delta;

    public LoopStartSectionCommand(Transport transport, double delta) {
        this.transport = transport;
        this.delta = delta;
    }

    @Override
    public void execute() {
        this.transport.getInPosition().inc(delta);
    }
}
