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
 * An immutable object that contains either a non-null reference to another object or contains nothing, i.e. absent.
 * It is never {@code null}.
 *
 * @param <T> - type of wrapped value
 */
public class Option<T> {

    private static final Option<?> ABSENT = new Option(null);

    private final T ref;

    Option(T ref) {
        this.ref = ref;
    }

    public static <T> Option<T> absent() {
        return (Option<T>) ABSENT;
    }

    public static <T> Option<T> of(T value) {
        return new Option<T>(value);
    }

    public boolean isPresent() {
        return ref != null;
    }

    public T or(T alternative) {
        return isPresent() ? ref : alternative;
    }

    public T orNull() {
        return isPresent() ? ref : null;
    }

    public T get() {
        if (ref == null) {
            throw new IllegalStateException("No value");
        } else {
            return ref;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (ref != null ? !ref.equals(option.ref) : option.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public String toString() {
        if (isPresent()) {
            return "Option{" + ref + '}';
        } else {
            return "Option{ absent }";
        }
    }

}
