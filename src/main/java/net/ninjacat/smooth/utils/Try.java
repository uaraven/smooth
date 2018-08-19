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
     * Executes code supplied as {@link Callable}.
     *
     * @param code Callable to execute
     * @param <T>  type of resulting value
     * @return Either {@link Try.Success} if execution was successful or {@link Try.Failure} if
     * execution has thrown the exception.
     */
    public static <T> Try<T> execute(final Callable<T> code) {
        try {
            return success(code.call());
        } catch (final Throwable thr) {
            return failure(thr);
        }
    }

    /**
     * Creates a successful Try instance.
     *
     * @param value Value of the Try
     * @param <T>   Type of the value
     * @return Successful result
     */
    public static <T> Try<T> success(final T value) {
        return new Success<>(value);
    }


    /**
     * Creates a failed Try instance.
     *
     * @param fail Throwable of the failure
     * @param <T>  Type of the possible result
     * @return Failed result
     */
    public static <T> Try<T> failure(final Throwable fail) {
        return new Failure<>(fail);
    }

    /**
     * <p>Executes unary function</p>
     * <p>Usage:<br> {@code Try.execute(function).with(42);}</p>
     * Allows to create a
     *
     * @param func function to be executed
     * @param <P>  type of parameter
     * @param <T>  type of result
     * @return delayed function execution which can be executed by supplying it with parameter.
     */
    public static <P, T> FunctionExecutor<T, P> execute(final Func<T, P> func) {
        return new FunctionExecutor<>(func);
    }

    /**
     * @return {@code true} if execution was successful
     */
    public abstract boolean isSuccessful();

    /**
     * @return value of the execution
     * @throws IllegalStateException if execution was not successful. Use {@link #isSuccessful()} to check execution status.
     */
    public abstract T getValue();

    /**
     * @return exception which caused failure
     * @throws IllegalStateException if execution was successful. Use {@link #isSuccessful()} to check execution status.
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
     * Try&lt;Integer&gt; result = Try.execute(DeepThought).with("What is the answer to the ultimate question?").then(interpretAnswer);
     * </code>
     * </blockquote>
     * This code will execute function DeepThough which accepts string as a parameter and then pass its result to function interpretAnswer, which
     * returns int.
     * <p>If both of the functions execute successfully then result will be {@link Try.Success} containing an integer,
     * if any of the functions fail with exception then result will be {@link Try.Failure} containing exception. This
     * code will never crash.</p>
     * <p>
     *
     * @param mapper function which accepts value from this object.
     * @param <S>    type of returning value of mapper function
     * @return result of execution of mapper function wrapped in Try
     */
    public <S> Try<S> then(final Func<S, T> mapper) {
        if (isSuccessful()) {
            return Try.execute(mapper).with(getValue());
        } else {
            return new Failure<>(getFailure());
        }
    }

    /**
     * Tries to recover from failure with provided {@link Callable}.
     * If original Try.execute() call was successful, no recovery is performed
     *
     * @param recoverCode Callable to call in the event of failure
     * @return Either {@link Try.Success} or {@link Try.Failure}
     */
    public Try<T> recover(final Callable<T> recoverCode) {
        if (isSuccessful()) {
            return this;
        } else {
            return Try.execute(recoverCode);
        }
    }


    /**
     * Retrieves result of computation from Try wrapped in {@link Option}.
     * If execution was successful it will return it, otherwise {@link Option#absent()} will be returned.
     *
     * @return Optional result of computation.
     */
    public Option<T> get() {
        if (isSuccessful()) {
            return Option.of(getValue());
        } else {
            return Option.absent();
        }
    }

    /**
     * Represents successful execution result
     *
     * @param <T> type of wrapped result
     */
    static final class Success<T> extends Try<T> {
        private final T value;

        Success(final T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public T getValue() {
            return this.value;
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

        Failure(final Throwable failure) {
            this.failure = failure;
        }

        @Override
        public Throwable getFailure() {
            return this.failure;
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

    /**
     * Delayed function executor. Allows to supply parameter to function before it is executed inside
     * {@link Try}.
     *
     * @param <R> Function result type.
     * @param <P> Function parameter type.
     */
    public static final class FunctionExecutor<R, P> {
        private final Func<R, P> function;

        private FunctionExecutor(final Func<R, P> function) {
            this.function = function;
        }

        /**
         * Supplies a parameter to a function which will be executed within {@link Try}
         *
         * @param parameter Parameter to pass to the function
         * @return result of the function execution wrapped in {@link Try}
         */
        public Try<R> with(final P parameter) {
            try {
                return success(this.function.apply(parameter));
            } catch (final Throwable thr) {
                return failure(thr);
            }
        }
    }
}
