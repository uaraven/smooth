/*
 * Copyright 2014 Oleksiy Voronin <ovoronin@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.ninjacat.smooth.collections;

import net.ninjacat.smooth.functions.Func;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListsTest {

    @Test
    public void shouldMapList() throws Exception {
        List<Integer> intList = Lists.of(1, 2, 3);

        List<Integer> sqList = Lists.map(intList, new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * integer;
            }
        });

        assertEquals("Result should be of same size as source list", intList.size(), sqList.size());

        for (int i = 0; i < intList.size(); i++) {
            assertEquals(i + " element is invalid", Integer.valueOf(intList.get(i) * intList.get(i)), sqList.get(i));
        }
    }
}
