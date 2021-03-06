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

import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import java.util.Iterator;

public class IterFixtures {
    static <T> void verifyNext(Iterator<T> iter, T elem) {
        Assert.assertThat(iter.hasNext(), CoreMatchers.is(true));
        Assert.assertThat(iter.next(), CoreMatchers.is(elem));
    }

    static <T> void verifyNoNext(Iterator<T> iter) {
        Assert.assertThat(iter.hasNext(), CoreMatchers.is(false));
    }

    public static class SideEffect {
        private boolean sideEffects = false;

        void sideEffect() {
            sideEffects = true;
        }

        boolean hasSideEffects() {
            return sideEffects;
        }
    }
}
