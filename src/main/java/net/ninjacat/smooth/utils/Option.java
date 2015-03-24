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

/**
 * An immutable object that contains either a non-null reference to another object or contains nothing, i.e. absent.
 * It is never {@code null}.
 *
 * @param <T> - type of wrapped value
 */
@SuppressWarnings("unchecked")
public class Option<T> {

    private static final Option<?> ABSENT = new Option(null);

    private final T ref;

    Option(final T ref) {
        this.ref = ref;
    }

    /**
     * Creates a new option that does not hold any value.
     *
     * @param <T> Type of optional value.
     * @return A new empty optional value.
     */
    public static <T> Option<T> absent() {
        return (Option<T>) ABSENT;
    }

    /**
     * Creates a new option from supplied value. Value can be null.
     *
     * @param value Value to wrap into Option
     * @param <T>   Type of optional value.
     * @return A new optional value.
     */
    public static <T> Option<T> of(final T value) {
        return new Option<T>(value);
    }

    /**
     * @return {@code true} if this Option is not absent, or {@code false} otherwise.
     */
    public boolean isPresent() {
        return null != this.ref;
    }

    /**
     * Returns wrapped value, if this Option is not absent or supplied alternative otherwise.
     *
     * @param alternative Alternative value to return if this Option is absent.
     * @return Wrapped value.
     */
    public T or(final T alternative) {
        return isPresent() ? this.ref : alternative;
    }

    /**
     * @return Wrapped value or null if this Option is absent.
     */
    public T orNull() {
        return isPresent() ? this.ref : null;
    }

    /**
     * Retrieves wrapped value. If this Option is empty, then {@link NullPointerException} will be thrown.
     *
     * @return Wrapped value.
     * @throws java.lang.NullPointerException if Option is empty.
     */
    public T get() {
        if (null == this.ref) {
            throw new NullPointerException("No value");
        } else {
            return this.ref;
        }
    }

    /**
     * If this option is not empty, its value is transformed with the given Function; otherwise, absent() is returned.
     * Function is permitted to return null, in this case absent() will be returned.
     * <p>
     * If function throws exception, absent() will be returned.
     * </p>
     *
     * @param transform Function {@link Func} to transform value from one type to another.
     * @param <K>       Type of the new value
     * @return Optional value (or absent Option).
     */
    public <K> Option<K> map(final Func<K, T> transform) {
        if (isPresent()) {
            return Try.execute(transform).with(this.ref).get();
        } else {
            return Option.absent();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;

        final Option option = (Option) o;

        if (null != this.ref ? !this.ref.equals(option.ref) : null != option.ref) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return null != this.ref ? this.ref.hashCode() : 0;
    }

    @Override
    public String toString() {
        if (isPresent()) {
            return "Option{" + this.ref + '}';
        } else {
            return "Option{ absent }";
        }
    }

}
