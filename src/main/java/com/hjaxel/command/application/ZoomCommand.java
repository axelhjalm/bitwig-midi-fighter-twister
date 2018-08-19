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

package com.hjaxel.command.application;

import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.Clip;
import com.hjaxel.command.BitwigCommand;

public class ZoomCommand implements BitwigCommand {

    private Application application;
    private int direction;
    private Clip clip;

    public ZoomCommand(Application application, int direction) {
        this.application = application;
        this.direction = direction;
        this.clip = clip;
    }

    @Override
    public void execute() {
        if (direction < 0) {
            application.zoomOut();
        }
        if (direction > 0) {
            application.zoomIn();
        }
    }

}
