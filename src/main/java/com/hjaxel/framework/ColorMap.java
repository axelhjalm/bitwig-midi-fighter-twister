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

package com.hjaxel.framework;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.SettableColorValue;

import java.util.ArrayList;
import java.util.List;

public class ColorMap {



    public static class TwisterColor {
        public final int twisterValue;
        public final float red;
        public final float green;
        public final float blue;


        @Override
        public String toString() {
            return red + ", " + green +", " + blue;
        }

        public TwisterColor(int twisterValue, float red, float green, float blue) {
            this.twisterValue = twisterValue;
            this.red = red/256;
            this.green = green/256;
            this.blue = blue/256;
        }

        public float distance(float red, float green, float blue){
            float r = this.red - red;
            float g = this.green - green;
            float b = this.blue - blue;
            return r*r + g*g + b*b;
        }
    }

    private List<TwisterColor> map = new ArrayList<>();

    public ColorMap() {
        map.add(new TwisterColor(1, 46, 39, 230));
        map.add(new TwisterColor(2, 45, 50, 229));
        map.add(new TwisterColor(3, 44, 61, 227));
        map.add(new TwisterColor(4, 45, 71, 228));
        map.add(new TwisterColor(5, 44, 82, 229));
        map.add(new TwisterColor(6, 43, 90, 229));
        map.add(new TwisterColor(7, 45, 100, 229));
        map.add(new TwisterColor(8, 43, 108, 228));
        map.add(new TwisterColor(9, 43, 116, 227));
        map.add(new TwisterColor(10, 43, 126, 228));
        map.add(new TwisterColor(11, 41, 136, 228));
        map.add(new TwisterColor(12, 39, 144, 227));
        map.add(new TwisterColor(13, 40, 154, 227));
        map.add(new TwisterColor(14, 38, 162, 226));
        map.add(new TwisterColor(15, 37, 171, 227));
        map.add(new TwisterColor(16, 35, 180, 227));
        map.add(new TwisterColor(17, 34, 189, 228));
        map.add(new TwisterColor(18, 32, 198, 227));
        map.add(new TwisterColor(19, 30, 207, 227));
        map.add(new TwisterColor(20, 28, 215, 225));
        map.add(new TwisterColor(21, 27, 225, 228));
        map.add(new TwisterColor(22, 27, 226, 219));
        map.add(new TwisterColor(23, 28, 226, 209));
        map.add(new TwisterColor(24, 27, 225, 200));
        map.add(new TwisterColor(25, 27, 226, 193));
        map.add(new TwisterColor(26, 27, 226, 185));
        map.add(new TwisterColor(27, 29, 226, 173));
        map.add(new TwisterColor(28, 28, 226, 165));
        map.add(new TwisterColor(29, 27, 226, 155));
        map.add(new TwisterColor(30, 28, 226, 147));
        map.add(new TwisterColor(31, 28, 226, 138));
        map.add(new TwisterColor(32, 29, 227, 130));
        map.add(new TwisterColor(33, 30, 227, 119));
        map.add(new TwisterColor(34, 29, 228, 111));
        map.add(new TwisterColor(35, 29, 228, 101));
        map.add(new TwisterColor(36, 29, 227, 90));
        map.add(new TwisterColor(37, 29, 227, 80));
        map.add(new TwisterColor(38, 29, 227, 72));
        map.add(new TwisterColor(39, 29, 226, 60));
        map.add(new TwisterColor(40, 28, 227, 48));
        map.add(new TwisterColor(41, 28, 227, 36));
        map.add(new TwisterColor(42, 31, 226, 23));
        map.add(new TwisterColor(43, 41, 227, 20));
        map.add(new TwisterColor(44, 55, 226, 23));
        map.add(new TwisterColor(45, 65, 227, 22));
        map.add(new TwisterColor(46, 74, 227, 22));
        map.add(new TwisterColor(47, 85, 227, 19));
        map.add(new TwisterColor(48, 94, 226, 19));
        map.add(new TwisterColor(49, 104, 228, 20));
        map.add(new TwisterColor(50, 112, 227, 20));
        map.add(new TwisterColor(51, 123, 227, 19));
        map.add(new TwisterColor(52, 131, 227, 16));
        map.add(new TwisterColor(53, 141, 227, 16));
        map.add(new TwisterColor(54, 151, 227, 15));
        map.add(new TwisterColor(55, 158, 226, 15));
        map.add(new TwisterColor(56, 167, 228, 13));
        map.add(new TwisterColor(57, 176, 227, 10));
        map.add(new TwisterColor(58, 183, 227, 8));
        map.add(new TwisterColor(59, 193, 227, 8));
        map.add(new TwisterColor(60, 202, 227, 4));
        map.add(new TwisterColor(61, 211, 227, 4));
        map.add(new TwisterColor(62, 220, 226, 2));
        map.add(new TwisterColor(63, 224, 224, 2));
        map.add(new TwisterColor(64, 227, 216, 10));
        map.add(new TwisterColor(65, 226, 206, 13));
        map.add(new TwisterColor(66, 227, 198, 18));
        map.add(new TwisterColor(67, 226, 189, 20));
        map.add(new TwisterColor(68, 225, 179, 23));
        map.add(new TwisterColor(69, 226, 171, 25));
        map.add(new TwisterColor(70, 227, 163, 29));
        map.add(new TwisterColor(71, 228, 154, 31));
        map.add(new TwisterColor(72, 228, 145, 31));
        map.add(new TwisterColor(73, 226, 138, 32));
        map.add(new TwisterColor(74, 226, 127, 33));
        map.add(new TwisterColor(75, 226, 120, 35));
        map.add(new TwisterColor(76, 226, 110, 35));
        map.add(new TwisterColor(77, 226, 101, 35));
        map.add(new TwisterColor(78, 227, 93, 37));
        map.add(new TwisterColor(79, 226, 84, 38));
        map.add(new TwisterColor(80, 227, 75, 38));
        map.add(new TwisterColor(81, 227, 67, 41));
        map.add(new TwisterColor(82, 227, 58, 39));
        map.add(new TwisterColor(83, 228, 47, 38));
        map.add(new TwisterColor(84, 229, 44, 48));
        map.add(new TwisterColor(85, 228, 47, 56));
        map.add(new TwisterColor(86, 228, 46, 68));
        map.add(new TwisterColor(87, 226, 45, 76));
        map.add(new TwisterColor(88, 226, 44, 84));
        map.add(new TwisterColor(89, 228, 44, 96));
        map.add(new TwisterColor(90, 227, 43, 103));
        map.add(new TwisterColor(91, 228, 43, 111));
        map.add(new TwisterColor(92, 227, 42, 120));
        map.add(new TwisterColor(93, 227, 43, 131));
        map.add(new TwisterColor(94, 227, 41, 138));
        map.add(new TwisterColor(95, 228, 40, 150));
        map.add(new TwisterColor(96, 227, 40, 157));
        map.add(new TwisterColor(97, 227, 38, 166));
        map.add(new TwisterColor(98, 226, 38, 174));
        map.add(new TwisterColor(99, 227, 37, 185));
        map.add(new TwisterColor(100, 227, 36, 193));
        map.add(new TwisterColor(101, 228, 35, 202));
        map.add(new TwisterColor(102, 227, 34, 211));
        map.add(new TwisterColor(103, 227, 32, 220));
        map.add(new TwisterColor(104, 227, 31, 227));
        map.add(new TwisterColor(105, 219, 32, 227));
        map.add(new TwisterColor(106, 210, 31, 227));
        map.add(new TwisterColor(107, 201, 31, 228));
        map.add(new TwisterColor(108, 193, 32, 229));
        map.add(new TwisterColor(109, 185, 32, 228));
        map.add(new TwisterColor(110, 177, 33, 229));
        map.add(new TwisterColor(111, 167, 33, 228));
        map.add(new TwisterColor(112, 157, 32, 228));
        map.add(new TwisterColor(113, 149, 33, 228));
        map.add(new TwisterColor(114, 140, 33, 230));
        map.add(new TwisterColor(115, 132, 33, 229));
        map.add(new TwisterColor(116, 124, 34, 230));
        map.add(new TwisterColor(117, 115, 33, 231));
        map.add(new TwisterColor(118, 106, 34, 229));
        map.add(new TwisterColor(119, 97, 33, 231));
        map.add(new TwisterColor(120, 88, 34, 231));
        map.add(new TwisterColor(121, 79, 33, 228));
        map.add(new TwisterColor(122, 70, 34, 228));
        map.add(new TwisterColor(123, 61, 34, 230));
        map.add(new TwisterColor(124, 51, 33, 229));
        map.add(new TwisterColor(125, 29, 226, 173));
        map.add(new TwisterColor(126, 28, 226, 165));
        map.add(new TwisterColor(127, 27, 226, 155));
    }


    public TwisterColor get(SettableColorValue color){
        return get(color.red(), color.green(), color.blue());
    }

    public TwisterColor get(float red, float green, float blue){
        TwisterColor bestMatch = new TwisterColor(0, 0, 0, 0);
        float v = bestMatch.distance(red, green, blue);
        for (TwisterColor c : map) {
            float distance = c.distance(red, green, blue);
            if(distance < v){
                bestMatch = c;
                v = distance;
            }
        }
        return bestMatch;
    }

    public TwisterColor get(int value){
        for (TwisterColor twisterColor : map) {
            if(twisterColor.twisterValue == value){
                return twisterColor;
            }
        }

        return map.get(0);
    }

}
