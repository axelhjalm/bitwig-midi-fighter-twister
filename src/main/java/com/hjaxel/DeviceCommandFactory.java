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

package com.hjaxel;

import com.bitwig.extension.controller.api.*;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.device.*;
import com.hjaxel.command.transport.ScrollPlayHeadCommand;
import com.hjaxel.navigation.CursorNavigator;

public class DeviceCommandFactory {

    private final CursorRemoteControlsPage remoteControlsPage;
    private final CursorDevice cursorDevice;
    private final PopupBrowser browser;
    private final CursorNavigator devices;
    private final CursorNavigator parameters;

    public DeviceCommandFactory(CursorRemoteControlsPage remoteControlsPage, CursorDevice cursorDevice, PopupBrowser browser, SettableRangedValue navigation) {
        this.remoteControlsPage = remoteControlsPage;
        this.cursorDevice = cursorDevice;
        this.browser = browser;
        this.devices = new CursorNavigator(cursorDevice, navigation);
        this.parameters = new CursorNavigator(remoteControlsPage, navigation);
    }

    public ChangeParameterCommand parameter(int index, double scale, int delta){
        RemoteControl remoteControl = remoteControlsPage.getParameter(index);
        return new ChangeParameterCommand(remoteControl, scale, delta);
    }

    public ScrollPresetsCommand scrollPresetsCommand(int direction){
        return new ScrollPresetsCommand(cursorDevice, browser, direction);
    }

    public SelectPresetsCommand selectPresetsCommand(){
        return new SelectPresetsCommand(browser);
    }

    public ToggleDeviceCommand toggleDeviceCommand(){
        return new ToggleDeviceCommand(cursorDevice);
    }

    public ToggleDisplayDeviceCommand toggleDisplayDeviceCommand(){
        return new ToggleDisplayDeviceCommand(cursorDevice);
    }

    public BitwigCommand scrollDevice(int direction) {
        return () -> devices.onChange(64 + direction);
    }

    public BitwigCommand scrollParameterPage(int direction) {
        return () -> parameters.onChange(64 + direction);
    }
}
