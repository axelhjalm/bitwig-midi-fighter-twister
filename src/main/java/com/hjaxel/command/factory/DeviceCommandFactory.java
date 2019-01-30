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

import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.PopupBrowser;
import com.bitwig.extension.controller.api.RemoteControl;
import com.hjaxel.UserSettings;
import com.hjaxel.command.BitwigCommand;
import com.hjaxel.command.device.*;
import com.hjaxel.navigation.CursorNavigator;

import java.util.function.Consumer;

public class DeviceCommandFactory {

    private final CursorRemoteControlsPage remoteControlsPage;
    private final CursorDevice cursorDevice;
    private final PopupBrowser browser;
    private final CursorNavigator devices;
    private final CursorNavigator parameters;

    public DeviceCommandFactory(CursorRemoteControlsPage remoteControlsPage, CursorDevice cursorDevice, PopupBrowser browser, UserSettings settings) {
        this.remoteControlsPage = remoteControlsPage;
        this.cursorDevice = cursorDevice;
        this.browser = browser;
        this.devices = new CursorNavigator(cursorDevice, settings);
        this.parameters = new CursorNavigator(remoteControlsPage, settings);
    }

    public ChangeParameterCommand parameter(int index, double scale, int delta) {
        RemoteControl remoteControl = remoteControlsPage.getParameter(index);
        return new ChangeParameterCommand(remoteControl, scale, delta);
    }

    public ChangeParameterCommand parameter(int index, double scale, int delta, Consumer<String> s) {
        RemoteControl remoteControl = remoteControlsPage.getParameter(index);
        s.accept(remoteControl.name().get());

        return new ChangeParameterCommand(remoteControl, scale, delta);
    }

    public ResetParameterCommand resetParameterCommand(int index) {
        RemoteControl remoteControl = remoteControlsPage.getParameter(index);
        return new ResetParameterCommand(remoteControl);
    }

    public ScrollPresetsCommand scrollPresetsCommand(int direction) {
        return new ScrollPresetsCommand(cursorDevice, browser, direction);
    }

    public SelectPresetsCommand selectPresetsCommand() {
        return new SelectPresetsCommand(browser);
    }

    public ToggleRemoteControlsCommand toggleRemoteControlsCommand() { return new ToggleRemoteControlsCommand(cursorDevice); }

    public ToggleDeviceCommand toggleDeviceCommand() {
        return new ToggleDeviceCommand(cursorDevice);
    }

    public ToggleDisplayDeviceCommand toggleDisplayDeviceCommand() {
        return new ToggleDisplayDeviceCommand(cursorDevice);
    }

    public BitwigCommand scrollDevice(int direction) {
        return () -> {
            devices.onChange(64 + direction);
            cursorDevice.selectInEditor();
        };
    }

    public BitwigCommand scrollParameterPage(int direction) {
        return () ->
        {
            cursorDevice.selectInEditor();
            parameters.onChange(64 + direction);
        };
    }
}
