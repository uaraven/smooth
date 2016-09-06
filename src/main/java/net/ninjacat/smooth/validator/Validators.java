package net.ninjacat.smooth.validator;

/**
 * Collection of functions to perform various validations
 */
public final class Validators {

    private Validators() {
    }

    /**
     * Validates supplied value. Throws supplied runtime exception if value is null
     *
     * @param o         object reference to check for null
     * @param onFailure Exception which is thrown if validation fails
     * @throws E if validation fails
     */
    public static <T, E extends Exception> void validateNotNull(final T o, final E onFailure) throws E {
        if (Predicates.matchesNull().matches(o)) {
            throw onFailure;
        }
    }

    /**
     * Validates supplied value. Throws supplied runtime exception if value is not null
     *
     * @param o         object reference to check for null
     * @param onFailure Exception which is thrown if validation fails
     * @throws E if validation fails
     */
    public static <T, E extends Exception> void validateNull(final T o, final E onFailure) throws E {
        if (!Predicates.matchesNull().matches(o)) {
            throw onFailure;
        }
    }

    /**
     * Validates supplied value. Throws {@link java.lang.NullPointerException} if value is null
     *
     * @param o object reference to check for null
     */
    public static <T> void validateNotNull(final T o) {
        if (Predicates.matchesNull().matches(o)) {
            throw new NullPointerException("Value should not be null");
        }
    }


    /**
     * Validates supplied value. Throws {@link java.lang.IllegalStateException} if value is not null
     *
     * @param o object reference to check for null
     */
    public static <T> void validateNull(final T o) {
        if (!Predicates.matchesNull().matches(o)) {
            throw new IllegalStateException("Value should be null");
        }
    }

}
