package net.ninjacat.smooth.validator;

import net.ninjacat.smooth.functions.Predicate;

/**
 * Collection of predicates to perform various validations
 */
final class Predicates {
    private static final Predicate IS_NULL = new Predicate() {
        @Override
        public boolean matches(Object t) {
            return null == t;
        }
    };

    private Predicates() {
    }

    /**
     * Creates a predicate that matches its argument against null.
     *
     * @return {@link Predicate} which check its argument for null.
     */
    public static Predicate matchesNull() {
        return IS_NULL;
    }

}

