package net.ninjacat.smooth.validator;

import net.ninjacat.smooth.functions.Predicate;

/**
 * Collection of predicates to perform various validations
 */
public class Predicates {
    private static final Predicate IS_NULL = new Predicate() {
        @Override
        public boolean matches(Object t) {
            return t == null;
        }
    };

    public static <T> Predicate<T> matchesNull() {
        return IS_NULL;
    }

}
