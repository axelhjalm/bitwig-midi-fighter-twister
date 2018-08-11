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

package com.hjaxel.command.track;

import com.bitwig.extension.controller.api.Send;
import com.bitwig.extension.controller.api.SendBank;
import com.hjaxel.command.BitwigCommand;

import java.util.function.Consumer;

public class SendCommand implements BitwigCommand {

    private final int index;
    private final int value;
    private Consumer<String> c;
    private SendBank bank;

    public SendCommand(SendBank bank, int index, int value, Consumer<String> c) {
        this.bank = bank;
        this.index = index;
        this.value = value;
        this.c = c;
    }

    @Override
    public void execute() {
        Send item = bank.getItemAt(index);
        item.set(value, 128);
    }

    @Override
    public String toString() {
        return "SendCommand " + index;
    }
}
