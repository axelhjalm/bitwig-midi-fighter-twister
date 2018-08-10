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
import com.bitwig.extension.controller.api.PopupBrowser;
import com.hjaxel.command.BitwigCommand;

public class ScrollPresetsCommand implements BitwigCommand {

    private CursorDevice cursorDevice;
    private final PopupBrowser browser;
    private int direction;

    public ScrollPresetsCommand(CursorDevice cursorDevice, PopupBrowser browser, int direction) {
        this.cursorDevice = cursorDevice;
        this.browser = browser;
        this.direction = direction;
    }

    @Override
    public void execute() {
        cursorDevice.browseToReplaceDevice();
        if (direction > 0) {
            browser.selectNextFile();
        } else if (direction < 0) {
            browser.selectPreviousFile();
        }
    }
}
