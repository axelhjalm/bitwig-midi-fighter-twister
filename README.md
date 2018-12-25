# bitwig-midi-fighter-twister
Bitwig extension for midi fighter twister. Requires Bitwig 2.3

## Installation
Put the MidiFighterTwister.bwextension file under the Bitwig Extension folder as per the documentation. Load the Midi Fighter Twister settings found in the .mfs file
    
## Functionality

         +-------------+
    TL   |1   2   3   4|  TR  
    ML   |5   6   7   8|  MR
    BL   |9  10  11  12|  BR
         |13 14  15  16|
         +-------------+
(TL = top left side button, ML = middle left, BL = bottom left and on)    

### Track navigation and device control
In track navigation and device control mode, the knobs works as follows
First row, from left to right
1: Scroll through tracks, press to toggle solo
2: Track volume, press to toggle mute
3: Track pan, press to reset to middle  
4: Position in track, press to play / stop

Second row, from left to right
5: Scroll through selected device preset pages, press to open / close remote controls panel
6: Scroll through devices in selected track, press to open / close device window
7: Press to disable device
8: Scroll to open preset browsing (presets in Bitwig only), press to select preset

Third and forth row:
Controls parameter in device preset page. Turn know to control parameter, press to toggle between coarse and fine control. Outer ring shows current value and is updated when stepping through parameter pages or devices.

###  Parameters 
Under Settings > Controllers are setting for the Midi Fighter Twister Controller. These are:
* Coarse and Fine Control Scale - Settable between 1 (fastest) and 12 (slowest), to change how fast macro control knobs change.
* Navigation Cursor Scroll speed - Controls the speed for "navigation" knobs
* Debug Log - enable or disable console debug logging (disabled by default)

### Mix / Send 
Press BL (Bottom Left button) to go to mix page. Press top left to go to device page

The eight rightmost knobs controls Send 1 - Send 8, press to set value of that send to 0
1-4. Same as device page (scroll track, volume, pan, play head and play/stop)
5: Start of loop section, press to toggle loop
6: End of loop section, press to toggle loop
7: Zoom in / out in selected window (arranger / clip), press to zoom to fit. Need to select arranger window or clip.
8: Select color of track/send
9-16: Send amount. Will use the color of the FX track. Press to reset to zero

### 16 track volume mixer
Press TR (top right) to access. Here you can adjust volume (turn knob) and solo tracks (press button).
User top left and bottom left buttons to scroll 16 tracks at a time.

## Licensing
 Bitwig Extension for Midi Fighter Twister
 Copyright (C) 2017 Axel Hjälm
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>
