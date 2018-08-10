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

package com.hjaxel.command.factory;

import com.bitwig.extension.controller.api.Transport;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.transport.LoopEndSectionCommand;
import com.hjaxel.command.transport.LoopStartSectionCommand;
import com.hjaxel.command.transport.PlayCommand;
import com.hjaxel.command.transport.ScrollPlayHeadCommand;

public class TransportCommandFactory {

    private Transport transport;

    public TransportCommandFactory(Transport transport) {
        this.transport = transport;
    }

    public ScrollPlayHeadCommand playHeadCommand(int delta){
        return new ScrollPlayHeadCommand(transport, delta);
    }

    public BitwigCommand loopStart(int value){
        return new LoopStartSectionCommand(transport, value);
    }

    public BitwigCommand loopEnd(int value){
        return new LoopEndSectionCommand(transport, value);
    }

    public BitwigCommand play() {
        return new PlayCommand(transport);
    }

    public BitwigCommand loopToggle() {
        return () -> transport.isArrangerLoopEnabled().toggle();
    }
}
