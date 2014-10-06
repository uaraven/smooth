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
        Try<Boolean> actual = Try.execute(new Callable<Boolean>() {
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
        Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });

        assertEquals((int) actual.getValue(), 42);
    }


    @Test
    public void shouldGetActualThrowableFromFailure() throws Exception {
        Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new NumberFormatException();
            }
        });

        assertTrue(actual.getFailure() instanceof NumberFormatException);
    }

    @Test
    public void unsuccessfulExecutionShouldReturnFailure() throws Exception {
        Try<Boolean> actual = Try.execute(new Callable<Boolean>() {
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
        Try<Integer> actual = Try.execute(new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return 42 + integer;
            }
        }).with(42);

        assertEquals((int) actual.getValue(), 84);
    }

    @Test
    public void mapShouldMapValues() throws Exception {
        Try<String> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        }).map(new Func<String, Integer>() {
            @Override
            public String apply(Integer integer) {
                return String.valueOf(integer);
            }
        });

        assertEquals(actual.getValue(), "42");
    }

    @Test
    public void mapShouldPropagateErrors() throws Exception {
        Try<String> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new NumberFormatException();
            }
        }).map(new Func<String, Integer>() {
            @Override
            public String apply(Integer integer) {
                return String.valueOf(integer);
            }
        });

        assertTrue(actual.getFailure() instanceof NumberFormatException);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingFailureFromSuccess() throws Exception {
        Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });

        actual.getFailure();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingValueFromFailure() throws Exception {
        Try<Integer> actual = Try.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalAccessException();
            }
        });

        actual.getValue();
    }

}
