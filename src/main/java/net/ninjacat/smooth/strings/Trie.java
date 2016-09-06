package net.ninjacat.smooth.strings;

import net.ninjacat.smooth.utils.Option;

import java.util.Set;

/**
 * TODO: Write JavaDoc
 */
public interface Trie<T> {
    void put(String text, T value);

    boolean contains(String text);

    Set<String> keySet();

    Set<String> startingWith(String text);

    Option<T> get(String text);
}
