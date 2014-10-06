package net.ninjacat.smooth.functions;

/**
 * Predicate is a function of which can return either {@code true} or {@code false}
 */
public interface Predicate<T> {
    boolean matches(T t);
}
