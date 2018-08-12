# bitwig-midi-fighter-twister
Bitwig extension for midi fighter twister

## Overview
The extension currently offers two different views, or "pages"
1) Track navigation and device control
2) 16 pad drum sequencer with support for clip of one measure length
 
Navigation between the two pages are done by the middle side buttons. Left side button will navigate to device mode, single press on right middle button from device view will navigate to drum pad mode. Pressing middle right button again will navigate to next Midi Fighter Twister page for which no functionality is currently implemented.   

## Installation
Latest version:
https://github.com/axelhjalm/bitwig-midi-fighter-twister/releases/tag/0.1

Put the MidiFighterTwister.bwextension file under the Bitwig Extension folder as per the documentation. Load the Midi Fighter Twister settings found in the fil mft_bitwig.mfs file
    
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
5: Scroll through selected device preset pages
6: Scroll through devices in selected track, press to open / close device window
7: Press to disable device
7: Scroll to open preset browsing (presets in Bitwig only), press to select preset

Third and forth row:
Controls parameter in device preset page. Turn know to control parameter, press to toggle between coarse and fine control. Outer ring shows current value and is updated when stepping through parameter pages or devices.

###  Parameters (NEW IN 0.3)
Under Settings > Controllers are setting for the Midi Fighter Twister Controller. These are:
* Coarse and Fine Control Scale - Settable between 1 (fastest) and 12 (slowest), to change how fast macro control knobs change.
* Navigation Cursor Scroll speed - Controls the speed for "navigation" knobs
* Debug Log - enable or disable console debug logging (disabled by default)

### Mix / Send (NEW IN 0.5)
Press BL (Bottom Left button) to go to mix page. Press top left to go to device page

The eight rightmost knobs controls Send 1 - Send 8, press to set value of that send to 0
1-4. Same as device page (scroll track, volume, pan, play head and play/stop)
5: Start of loop section, press to toggle loop
6: End of loop section, press to toggle loop
7: Zoom in / out in selected window (arranger / clip), press to zoom to fit
8: Select color of track/send
9-16: Send amount. Will use the color of the FX track. Press to reset to zero

### 16 track volume mixer
Press TR (top right) to access. Here you can adjust volume (turn knob) and solo tracks (press button).
User top left and bottom left buttons to scroll 16 tracks at a time.

### 16 pad drum sequencer
This mode must be used on a clip the size of one measure (16 x 16th notes) together till Bitwig Drum Machine. Might work with other devices but has not been tested properly. 
Press a knob to enter step sequencer for that drum (knobs turn green). While in this view, press knobs to toggle the drum pad for the corresponding step. Twist not to set volume.
To return to drum pad selection, press lower left button. While playing, the LEDs will light up when the corresponding drum pad i triggered. 
To reset the clip, press the lower right button. Be warned that this will reset all notes in the selected midi clip! 

## Know limitations
1) Drum pad only supports one full measure
2) Switching between drumpad clips or other clips will not update the sequencer in the Midi Fighter Twister


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
