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
