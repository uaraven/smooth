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

package net.ninjacat.smooth.iterators;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static net.ninjacat.smooth.iterators.IterFixtures.verifyNext;
import static net.ninjacat.smooth.iterators.IterFixtures.verifyNoNext;

/**
 * User: raven
 * Date: 06/09/13
 */
public class MultiIterableTest {

    @Test
    public void shouldWalkSimpleCollection() throws Exception {
        MultiIterable<String> mi = new MultiIterable<String>();
        mi.append(Arrays.asList("a", "b", "c"));

        Iterator<String> iter = mi.iterator();
        verifyNext(iter, "a");
        verifyNext(iter, "b");
        verifyNext(iter, "c");
        verifyNoNext(iter);
    }

    @Test
    public void shouldWalkCombinedCollection() throws Exception {
        MultiIterable<String> mi = new MultiIterable<String>();
        mi.append(Arrays.asList("a", "b"));
        mi.append(Arrays.asList("c"));

        Iterator<String> iter = mi.iterator();
        verifyNext(iter, "a");
        verifyNext(iter, "b");
        verifyNext(iter, "c");
        verifyNoNext(iter);
    }

}
