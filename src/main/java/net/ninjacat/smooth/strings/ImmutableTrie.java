package net.ninjacat.smooth.strings;

import net.ninjacat.smooth.utils.Option;

import java.util.Set;

/**
 * String to any map implemented as trie.
 */
public class ImmutableTrie<T> implements Trie<T> {
    private final TrieNode<T> root;

    /**
     * Creates new instance of the Trie
     */
    public ImmutableTrie() {
        super();
        this.root = new TrieNode<>();
    }

    private ImmutableTrie(final TrieNode<T> root) {
        super();
        this.root = root;
    }

    @Override
    public void put(final String text, final T value) {
        throw new UnsupportedOperationException("Cannot change immutable Trie");
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

    public static class Builder<T> {
        private final TrieNode<T> root;

        public Builder() {
            this.root = new TrieNode<>();
        }

        public Builder<T> put(final String key, final T value) {
            this.root.put(key, value);
            return this;
        }

        public ImmutableTrie<T> build() {
            return new ImmutableTrie<>(this.root);
        }
    }
}
