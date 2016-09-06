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

/**
 * Selection of two values. This container can contain either the first or the second value.
 *
 * @param <F> type of first value
 * @param <S> type of second value
 */
public abstract class Either<F, S> {

    private Either() {
    }

    /**
     * Creates an new Either instance with the first value.
     *
     * @param val The first value.
     * @param <F> Type of the first value.
     * @param <S> Type of the second value.
     * @return Either holding the first value.
     */
    public static <F, S> Either<F, S> first(final F val) {
        return new First<F, S>(val);
    }

    /**
     * Creates an new Either instance with the second value.
     *
     * @param val The second value.
     * @param <F> Type of the first value.
     * @param <S> Type of the second value.
     * @return Either holding the second value.
     */
    public static <F, S> Either<F, S> second(final S val) {
        return new Second<F, S>(val);
    }

    /**
     * Checks if this container has the first value.
     *
     * @return {@code true} if this is the first value.
     */
    public abstract boolean hasFirst();

    /**
     * Checks if this container has the second value.
     * @return {@code true} if this is the second value.
     */
    public abstract boolean hasSecond();

    /**
     * Retrieve the first value. Will throw exception if in this instance has no first value.
     * @return The first value.
     * @throws IllegalStateException if this instance has no first value.
     */
    public abstract F getFirst();

    /**
     * Retrieve the second value. Will throw exception if in this instance has no second value.
     * @return The second value.
     * @throws IllegalStateException if this instance has no second value.
     */
    public abstract S getSecond();

    /**
     * Container that only has the first value.
     * @param <F> Type of the first value.
     * @param <S> Type of the second value.
     */
    public static final class First<F, S> extends Either<F, S> {
        private final F first;

        private First(final F first) {
            this.first = first;
        }

        @Override
        public boolean hasFirst() {
            return true;
        }

        @Override
        public boolean hasSecond() {
            return false;
        }

        @Override
        public F getFirst() {
            return this.first;
        }

        @Override
        public S getSecond() {
            throw new IllegalStateException("Has no second");
        }
    }

    /**
     * Container that only has the second value.
     * @param <F> Type of the first value.
     * @param <S> Type of the second value.
     */
    public static final class Second<F, S> extends Either<F, S> {
        private final S second;

        private Second(final S second) {
            this.second = second;
        }

        @Override
        public boolean hasFirst() {
            return false;
        }

        @Override
        public boolean hasSecond() {
            return true;
        }

        @Override
        public F getFirst() {
            throw new IllegalStateException("Has no first");
        }

        @Override
        public S getSecond() {
            return this.second;
        }
    }
}
