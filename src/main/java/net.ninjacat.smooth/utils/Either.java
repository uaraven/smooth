package net.ninjacat.smooth.utils;

/**
 * Created on 26/04/14.
 */
public class Either<F, S> {
    private final F first;
    private final S second;

    public static <F, S> Either<F, S> first(F val) {
        return new Either<F, S>(val, null);
    }

    public static <F, S> Either<F, S> second(S val) {
        return new Either<F, S>(null, val);
    }

    public Either(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public boolean hasFirst() {
        return first != null;
    }

    public boolean hasSecond() {
        return second != null;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
