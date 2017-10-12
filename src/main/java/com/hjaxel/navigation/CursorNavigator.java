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

package com.hjaxel.navigation;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Cursor;
import com.bitwig.extension.controller.api.Device;
import com.bitwig.extension.controller.api.PinnableCursorDevice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by axel on 2017-09-16.
 */
public class CursorNavigator {

    private final AtomicInteger value = new AtomicInteger(0);
    private final Cursor cursor;
    private final int scale;

    public CursorNavigator(Cursor cursor, int scale) {
        this.cursor = cursor;
        this.scale = scale;
    }

    public void onChange(int val){
        if (cursor == null){
            return;
        }
        if (value.getAndIncrement() % scale != 0){
            return;
        }
        if (val == 63){
            cursor.selectPrevious();
        }

        if (val == 65){
            cursor.selectNext();
        }

    }


}
