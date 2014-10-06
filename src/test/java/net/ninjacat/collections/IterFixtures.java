package net.ninjacat.collections;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import java.util.Iterator;

public class IterFixtures {
    static <T> void verifyNext(Iterator<T> iter, T elem) {
        Assert.assertThat(iter.hasNext(), CoreMatchers.is(true));
        Assert.assertThat(iter.next(), CoreMatchers.is(elem));
    }

    static <T> void verifyNoNext(Iterator<T> iter) {
        Assert.assertThat(iter.hasNext(), CoreMatchers.is(false));
    }

    public static class SideEffect {
        private boolean sideEffects = false;

        void sideEffect() {
            sideEffects = true;
        }

        boolean hasSideEffects() {
            return sideEffects;
        }
    }
}