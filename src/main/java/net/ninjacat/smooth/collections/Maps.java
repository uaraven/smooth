package net.ninjacat.smooth.collections;

import net.ninjacat.smooth.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public final class Maps {

    private Maps() {
    }

    /**
     * <p>
     * Will create map, give list of key, value pairs.
     * </p>
     * <p>
     * Example: {@code Map<Integer, String> map = Maps.of(1, "One", 2, "Two", 3, "Three");}
     * </p>
     * <p>
     * <strong>Number of parameters must be even.</strong>
     * <strong>Will throw ClassCastException if wrong argument types are supplied</strong>
     * </p>
     *
     * @param pairs list of key-value pairs. There is no special separation of each pair.
     * @param <K>   Type of keys
     * @param <V>   Type of values
     * @return Map&lt;K,V&gt; of supplied key-value pairs
     */
    public static <K, V> Map<K, V> of(final Object... pairs) {
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Initializer should have even number of elements");
        }
        final Map<K, V> map = new HashMap<K, V>();

        for (int i = 0; i < pairs.length; i += 2) {
            map.put((K) pairs[i], (V) pairs[i + 1]);
        }

        return map;
    }

    /**
     * <p>
     * Will create map, give list of key-value pairs.
     * </p>
     * <p>
     * Pairs are represented by {@link Pair} objects where left is used as key and right is used for value.
     * </p>
     *
     * @param pairs list of key-value pairs.
     * @param <K>   Type of keys
     * @param <V>   Type of values
     * @return Map&lt;K,V&gt; of supplied key-value pairs
     */
    public static <K, V> Map<K, V> of(final Pair<K, V>... pairs) {
        final Map<K, V> map = new HashMap<K, V>();

        for (final Pair<K, V> pair : pairs) {
            map.put(pair.getLeft(), pair.getRight());
        }

        return map;
    }
}
