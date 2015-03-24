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

package net.ninjacat.smooth.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EitherTest {

    @Test
    public void shouldReportFirstPresent() throws Exception {
        Either<Integer, Integer> first = Either.first(10);

        assertTrue("Should have first", first.hasFirst());
    }

    @Test
    public void shouldReportSecondPresent() throws Exception {
        Either<Integer, Integer> one = Either.second(10);

        assertTrue("Should have second", one.hasSecond());
    }

    @Test
    public void shouldReturnCorrectFirstValue() throws Exception {
        Either<Integer, Integer> one = Either.first(10);

        assertEquals("Should have first value", 10, one.getFirst().intValue());
    }

    @Test
    public void shouldReturnCorrectSecondValue() throws Exception {
        Either<Integer, Integer> one = Either.second(20);

        assertEquals("Should have second value", 20, one.getSecond().intValue());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenRetrievingSecondValueFromFirstEither() throws Exception {
        Either.first(10).getSecond();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenRetrievingFirstValueFromSecondEither() throws Exception {
        Either.second(10).getFirst();
    }

}
