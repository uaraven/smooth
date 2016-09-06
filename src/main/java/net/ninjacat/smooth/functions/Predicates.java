package net.ninjacat.smooth.functions;

/**
 * Useful predicates
 */
public final class Predicates {
    private Predicates() {
    }

    /**
     * Inverts result of predicate.
     *
     * @param predicate Predicate to invert result.
     * @param <T>       Value type.
     * @return Inverted predicate.
     */
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return new Predicate<T>() {
            @Override
            public boolean matches(final T t) {
                return !predicate.matches(t);
            }
        };
    }

    /**
     * Predicate that checks value for null.
     *
     * @param <T> Value type.
     * @return Predicate that returns {code true} if value is null.
     */
    public static <T> Predicate<T> isNull() {
        return new Predicate<T>() {
            @Override
            public boolean matches(T t) {
                return null == t;
            }
        };
    }


    /**
     * Predicate that checks value for null.
     *
     * @param <T> Value type.
     * @return Predicate that returns {code true} if value is not null.
     */
    public static <T> Predicate<T> isNotNull() {
        return new Predicate<T>() {
            @Override
            public boolean matches(T t) {
                return null != t;
            }
        };
    }

    /**
     * Predicate that evaluates to true if the class being tested is assignable to the given class.
     *
     * @param <T> Value type.
     * @param cls Class to be tested against.
     * @return Predicate that returns {code true} if value can be assigned to class.
     */
    public static <T> Predicate<T> instanceOf(final Class cls) {
        return new Predicate<T>() {
            @Override
            public boolean matches(T t) {
                return cls.isAssignableFrom(t.getClass());
            }
        };
    }

    /**
     * Predicate that evaluates to true if the class being tested is assignable from the given class.
     *
     * @param <T> Value type.
     * @param cls Class to be tested against.
     * @return Predicate that returns {code true} if value can be assigned from class.
     */
    public static <T> Predicate<T> assignableFrom(final Class cls) {
        return new Predicate<T>() {
            @Override
            public boolean matches(T t) {
                return t.getClass().isAssignableFrom(cls);
            }
        };
    }

}
