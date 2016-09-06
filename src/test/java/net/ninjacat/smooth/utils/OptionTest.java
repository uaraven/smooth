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

import net.ninjacat.smooth.functions.Func;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created on 26/04/14.
 */
public class OptionTest {

    @Test(expected = NullPointerException.class)
    public void getShouldThrowExceptionWhenQueried() throws Exception {
        final Option<Object> option = Option.absent();

        option.get();
    }

    @Test
    public void getShouldReturnStoredValue() throws Exception {
        final Option<String> option = Option.of("Test");

        assertThat(option.get(), is("Test"));
    }

    @Test
    public void orShouldReturnStoredValueIfPresent() throws Exception {
        final Option<String> option = Option.of("Test");

        assertThat(option.or("None"), is("Test"));
    }

    @Test
    public void orShouldReturnAlternativeValueIfAbsent() throws Exception {
        final Option<String> option = Option.absent();

        assertThat(option.or("None"), is("None"));
    }

    @Test
    public void orNullShouldReturnNullIfAbsent() throws Exception {
        final Option<String> option = Option.absent();

        assertNull(option.orNull());
    }

    @Test
    public void orNullShouldReturnStoredValueIfPresent() throws Exception {
        final Option<String> option = Option.of("Test");

        assertThat(option.orNull(), is("Test"));
    }

    @Test
    public void isPresentShouldBeTrueIfValuePresent() throws Exception {
        final Option<String> option = Option.of("Test");
        assertTrue(option.isPresent());
    }


    @Test
    public void isPresentShouldBeFalseIfValueAbsent() throws Exception {
        final Option<String> option = Option.absent();
        assertFalse(option.isPresent());
    }

    @Test
    public void shouldTransformPresentOption() throws Exception {
        final Option<String> option = Option.of("42");
        final Option<Integer> mapped = option.map(new Func<Integer, String>() {
            @Override
            public Integer apply(final String s) {
                return Integer.parseInt(s);
            }
        });

        assertThat(mapped.isPresent(), is(true));
        assertThat(mapped.get(), is(42));
    }

    @Test
    public void shouldTransformAbsentOption() throws Exception {
        final Option<String> option = Option.absent();
        final Option<Integer> mapped = option.map(new Func<Integer, String>() {
            @Override
            public Integer apply(final String s) {
                return Integer.parseInt(s);
            }
        });

        assertThat(mapped.isPresent(), is(false));
    }

    @Test
    public void shouldTransformAbsentOptionWhenFunctionReturnsNull() throws Exception {
        final Option<String> option = Option.of("42");
        final Option<Integer> mapped = option.map(new Func<Integer, String>() {
            @Override
            public Integer apply(final String s) {
                return null;
            }
        });

        assertThat(mapped.isPresent(), is(false));
    }

    @Test
    public void shouldTransformAbsentOptionWhenFunctionThrowsException() throws Exception {
        final Option<String> option = Option.of("42");
        final Option<Integer> mapped = option.map(new Func<Integer, String>() {
            @Override
            public Integer apply(final String s) {
                throw new IllegalStateException();
            }
        });

        assertThat(mapped.isPresent(), is(false));
    }
}
