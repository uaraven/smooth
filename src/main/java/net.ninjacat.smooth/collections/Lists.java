package net.ninjacat.smooth.collections;

import net.ninjacat.smooth.functions.F;
import net.ninjacat.smooth.functions.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Lists {

    public static <E, R> List<R> map(List<E> list, F<R, E> mapFunc) {
        List<R> result = new ArrayList<R>(list.size());
        for (E e : list) {
            result.add(mapFunc.apply(e));
        }
        return Collections.unmodifiableList(result);
    }

    public static <E> List<E> filter(List<E> list, Predicate<E> filterFunc) {
        List<E> result = new ArrayList<E>();
        for (E e : list) {
            if (filterFunc.matches(e))
                result.add(e);
        }
        return Collections.unmodifiableList(result);
    }

    public static <T> List<T> of(T... elements) {
        return Arrays.asList(elements);
    }
}
