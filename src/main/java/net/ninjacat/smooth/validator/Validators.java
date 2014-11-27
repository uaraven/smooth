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
     * @param o object reference to check for null
     */
    public static void validateNotNull(Object o, RuntimeException onFailure) {
        if (Predicates.matchesNull().matches(o)) {
            throw onFailure;
        }
    }

    /**
     * Validates supplied value. Throws supplied runtime exception if value is not null
     *
     * @param o object reference to check for null
     */
    public static void validateNull(Object o, RuntimeException onFailure) {
        if (!Predicates.matchesNull().matches(o)) {
            throw onFailure;
        }
    }

    /**
     * Validates supplied value. Throws {@link java.lang.NullPointerException} if value is null
     *
     * @param o object reference to check for null
     */
    public static void validateNotNull(Object o) {
        if (Predicates.matchesNull().matches(o)) {
            throw new NullPointerException("Value should not be null");
        }
    }


    /**
     * Validates supplied value. Throws {@link java.lang.IllegalStateException} if value is not null
     *
     * @param o object reference to check for null
     */
    public static void validateNull(Object o) {
        if (!Predicates.matchesNull().matches(o)) {
            throw new IllegalStateException("Value should be null");
        }
    }

}
