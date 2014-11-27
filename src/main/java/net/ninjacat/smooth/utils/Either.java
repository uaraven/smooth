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
 * Selection of two values
 *
 * @param <F> type of first value
 * @param <S> type of second value
 */
public abstract class Either<F, S> {

    private Either() {
    }

    public static <F, S> Either<F, S> first(F val) {
        return new First<F, S>(val);
    }

    public static <F, S> Either<F, S> second(S val) {
        return new Second<F, S>(val);
    }

    public abstract boolean hasFirst();

    public abstract boolean hasSecond();

    public abstract F getFirst();

    public abstract S getSecond();

    public static final class First<F, S> extends Either<F, S> {
        private final F first;

        private First(F first) {
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
            return first;
        }

        @Override
        public S getSecond() {
            throw new IllegalStateException("Has no second");
        }
    }

    public static final class Second<F, S> extends Either<F, S> {
        private final S second;

        private Second(S second) {
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
            return second;
        }
    }
}
