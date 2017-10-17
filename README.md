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
1: Scroll through tracks 
2: Track volume
3: Track pan, press to reset to middle  
4: Play / stop

Second row, from left to right
5: Scroll through selected device preset pages
6: Scroll through devices in selected track
7: No functionality yet
7: No functionality yet

Third and forth row:
Controls parameter in device preset page. Turn know to control parameter, press and turn know to control more slowly. Outer ring shows current value and is updated when stepping through parameter pages or devices.

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
