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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created on 26/04/14.
 */
public class OptionTest {

    @Test(expected = IllegalStateException.class)
    public void getShouldThrowExceptionWhenQueried() throws Exception {
        Option<Object> option = Option.absent();

        option.get();
    }

    @Test
    public void getShouldReturnStoredValue() throws Exception {
        Option<String> option = Option.of("Test");

        assertThat(option.get(), is("Test"));
    }

    @Test
    public void orShouldReturnStoredValueIfPresent() throws Exception {
        Option<String> option = Option.of("Test");

        assertThat(option.or("None"), is("Test"));
    }

    @Test
    public void orShouldReturnAlternativeValueIfAbsent() throws Exception {
        Option<String> option = Option.absent();

        assertThat(option.or("None"), is("None"));
    }

    @Test
    public void orNullShouldReturnNullIfAbsent() throws Exception {
        Option<String> option = Option.absent();

        assertNull(option.orNull());
    }

    @Test
    public void orNullShouldReturnStoredValueIfPresent() throws Exception {
        Option<String> option = Option.of("Test");

        assertThat(option.orNull(), is("Test"));
    }

    @Test
    public void isPresentShouldBeTrueIfValuePresent() throws Exception {
        Option<String> option = Option.of("Test");
        assertTrue(option.isPresent());
    }


    @Test
    public void isPresentShouldBeFalseIfValueAbsent() throws Exception {
        Option<String> option = Option.absent();
        assertFalse(option.isPresent());
    }
}
