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

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class EncoderTest {

    @Test
    public void ensureUniquenessOfChannelAndCCs(){
        List<Encoder> encoders = Arrays.asList(Encoder.values());

        Map<Integer, Set<Integer>> channelToCCMap = new HashMap<>();

        encoders.forEach(e -> {
            channelToCCMap.putIfAbsent(e.getChannel().value(), new HashSet<>());
            int cc = e.getCc();
            assertFalse("Duplicate " + e, channelToCCMap.get(e.getChannel().value()).contains(cc));
            channelToCCMap.get(e.getChannel().value()).add(cc);

        });

    }

}