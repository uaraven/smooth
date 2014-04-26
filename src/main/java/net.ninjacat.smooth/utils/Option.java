package net.ninjacat.smooth.utils;

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


}
