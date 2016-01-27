package net.ninjacat.smooth.strings;

import net.ninjacat.smooth.utils.Option;

import java.util.Set;

/**
 * String to any map implemented as trie.
 */
public class MutableTrie<T> implements Trie<T> {
    private final TrieNode<T> root;

    /**
     * Creates new instance of the Trie
     */
    public MutableTrie() {
        super();
        this.root = new TrieNode<>();
    }

    @Override
    public void put(final String text, final T value) {
        this.root.put(text, value);
    }

    @Override
    public boolean contains(final String text) {
        return this.root.contains(text);
    }

    @Override
    public Set<String> keySet() {
        return this.root.toSet();
    }

    @Override
    public Set<String> startingWith(final String text) {
        return this.root.startingWith(text);
    }

    @Override
    public Option<T> get(final String text) {
        return this.root.get(text);
    }
}
