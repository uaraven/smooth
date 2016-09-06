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

import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * Created on 26/04/14.
 */
public class TryTest {

    @Test
    public void successfulExecutionShouldReturnSuccess() throws Exception {
        final Try<Boolean> actual = Try.execute(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });

        assertTrue(actual instanceof Try.Success);
        assertTrue(actual.isSuccessful());
    }

    @Test
    public void shouldGetActualResultFromSuccess() throws Exception {
        final Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });

        assertEquals((int) actual.getValue(), 42);
    }


    @Test
    public void shouldGetActualThrowableFromFailure() throws Exception {
        final Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new NumberFormatException();
            }
        });

        assertTrue(actual.getFailure() instanceof NumberFormatException);
    }

    @Test
    public void unsuccessfulExecutionShouldReturnFailure() throws Exception {
        final Try<Boolean> actual = Try.execute(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                throw new Exception();
            }
        });

        assertTrue(actual instanceof Try.Failure);
        assertFalse(actual.isSuccessful());
    }


    @Test
    public void shouldExecuteFunctionWithParameter() throws Exception {
        final Try<Integer> actual = Try.execute(new Func<Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer) {
                return 42 + integer;
            }
        }).with(42);

        assertEquals((int) actual.getValue(), 84);
    }

    @Test
    public void mapShouldMapValues() throws Exception {
        final Try<String> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        }).then(new Func<String, Integer>() {
            @Override
            public String apply(final Integer integer) {
                return String.valueOf(integer);
            }
        });

        assertEquals(actual.getValue(), "42");
    }

    @Test
    public void mapShouldPropagateErrors() throws Exception {
        final Try<String> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new NumberFormatException();
            }
        }).then(new Func<String, Integer>() {
            @Override
            public String apply(final Integer integer) {
                return String.valueOf(integer);
            }
        });

        assertTrue(actual.getFailure() instanceof NumberFormatException);
    }

    @Test
    public void shouldReturnAbsentOptionWhenFailed() throws Exception {
        final Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalStateException();
            }
        });

        assertFalse(actual.get().isPresent());
    }

    @Test
    public void shouldReturnOptionalResult() throws Exception {
        final Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });

        assertEquals(actual.get(), Option.of(42));
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingFailureFromSuccess() throws Exception {
        final Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });

        actual.getFailure();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingValueFromFailure() throws Exception {
        final Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalAccessException();
            }
        });

        actual.getValue();
    }

}
