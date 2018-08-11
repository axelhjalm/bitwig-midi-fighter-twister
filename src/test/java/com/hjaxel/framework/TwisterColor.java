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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TwisterColor {


    public static void main(String[] args) throws Exception {
        File file = new File("twister.bmp");
        BufferedImage image = ImageIO.read(file);


        List<Col> cols = new ArrayList<>();
        int z = 1;
        int width = image.getWidth();
        for (int i = 0; i < width; i = i + 10) {
            int rgb = image.getRGB(i, 10);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            z = z + 1;
            System.out.println(z);
            if (z % 2 == 0) {
                continue;
            } else {
                cols.add(new Col(red, green, blue));
            }


        }

        for (int i = 0; i < cols.size(); i++) {
            System.out.println("map.add(new TwisterColor(" + (i + 1) + ", " + cols.get(i) + "));");
        }

    }

    static class Col {
        int red, green, blue;

        @Override
        public String toString() {
            return red + ", " + green + ", " + blue;
        }

        public Col(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Col col = (Col) o;

            if (red != col.red) return false;
            if (green != col.green) return false;
            return blue == col.blue;
        }

        @Override
        public int hashCode() {
            int result = red;
            result = 31 * result + green;
            result = 31 * result + blue;
            return result;
        }
    }

}
