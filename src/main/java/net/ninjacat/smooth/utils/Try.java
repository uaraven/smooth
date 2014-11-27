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

import java.util.concurrent.Callable;

/**
 * Wrapper around potentially unsafe execution.
 */
public abstract class Try<T> {

    private Try() {

    }

    /**
     * Executes code supplied as {@link java.util.concurrent.Callable}.
     *
     * @param code Callable to execute
     * @param <T>  type of resulting value
     * @return Either {@link Try.Success} if execution was successful or {@link Try.Failure} if
     * execution has thrown the exception.
     */
    public static <T> Try<T> execute(Callable<T> code) {
        try {
            return new Success<T>(code.call());
        } catch (Throwable thr) {
            return new Failure<T>(thr);
        }
    }

    /**
     * <p>Executes unary function</p>
     * <p>Usage:<br> <code>Try.execute(function).with(42);</code></p>
     *
     * @param func function to be executed
     * @param <P>  type of parameter
     * @param <T>  type of result
     * @return delayed function execution which can be executed by supplying it with parameter.
     */
    public static <P, T> FunctionExecutor<T, P> execute(Func<T, P> func) {
        return new FunctionExecutor<T, P>(func);
    }

    /**
     * @return {@code true} if execution was successful
     */
    public abstract boolean isSuccessful();

    /**
     * @return value of the execution
     * @throws java.lang.IllegalStateException if execution was not successful. Use {@link #isSuccessful()} to check execution status.
     */
    public abstract T getValue();

    /**
     * @return exception which caused failure
     * @throws java.lang.IllegalStateException if execution was successful. Use {@link #isSuccessful()} to check execution status.
     */
    public abstract Throwable getFailure();

    /**
     * <p>
     * Allows chaining of several operations depending on result of the previous one. Hides complexity of error-handling and allows developer to
     * focus on happy path.
     * </p>
     * <p>
     * Usage example:
     * <blockquote>
     * <code>
     * Try&lt;Integer&gt; result = Try.execute(DeepThought).with("What is the answer to the ultimate question?").map(interpretAnswer);
     * </code>
     * </blockquote>
     * This code will execute function DeepThough which accepts string as a parameter and then pass its result to function interpretAnswer, which
     * returns int.
     * <p>If both of the functions execute successfully then result will be {@link Try.Success} containing an integer,
     * if any of the functions fail with exception then result will be {@link Try.Failure} containing exception. This
     * code will never crash.</p>
     * </p>
     *
     * @param mapper function which accepts value from this object.
     * @param <S>    type of returning value of mapper function
     * @return result of execution of mapper function wrapped in Try
     */
    public <S> Try<S> map(Func<S, T> mapper) {
        if (isSuccessful()) {
            return Try.execute(mapper).with(getValue());
        } else {
            return new Failure<S>(getFailure());
        }
    }

    /**
     * Represents successful execution result
     *
     * @param <T> type of wrapped result
     */
    static final class Success<T> extends Try<T> {
        private final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Throwable getFailure() {
            throw new IllegalStateException("Success does not contain failure");
        }
    }

    /**
     * Represents failed execution
     *
     * @param <T> type of expected successful result. Not used in this class
     */
    static final class Failure<T> extends Try<T> {
        private final Throwable failure;

        Failure(Throwable failure) {
            this.failure = failure;
        }

        @Override
        public Throwable getFailure() {
            return failure;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public T getValue() {
            throw new IllegalStateException("Failure does not contain result");
        }
    }

    public static final class FunctionExecutor<R, P> {
        private final Func<R, P> function;

        private FunctionExecutor(Func<R, P> function) {
            this.function = function;
        }

        public Try<R> with(P parameter) {
            try {
                return new Success<R>(function.apply(parameter));
            } catch (Throwable thr) {
                return new Failure<R>(thr);
            }
        }
    }
}
