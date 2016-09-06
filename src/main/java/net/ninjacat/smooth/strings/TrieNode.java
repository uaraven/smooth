package net.ninjacat.smooth.strings;

import net.ninjacat.smooth.utils.Option;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple mutable trie structure represented as recursive node.
 */
final class TrieNode<T> {
    private final Map<Character, TrieNode<T>> children;
    private Option<T> value;

    /**
     * Creates new instance of empty trie
     */
    TrieNode() {
        super();
        this.children = new ConcurrentHashMap<>();
        this.value = Option.absent();
    }

    /**
     * Creates new instance of empty trie
     */
    TrieNode(final String text, final T value) {
        super();
        this.children = new ConcurrentHashMap<>();
        this.value = Option.absent();
        put(text, value);
    }

    /**
     * Adds a string to a trie
     *
     * @param text
     */
    public void put(final String text, final T nodeValue) {
        if (text == null || text.isEmpty()) {
            if (this.value.isPresent()) {
                throw new IllegalStateException("Cannot modify trie node");
            }
            this.value = Option.of(nodeValue);
            return;
        }
        final Character head = text.charAt(0);
        final String tail = text.substring(1);
        if (this.children.containsKey(head)) {
            this.children.get(head).put(tail, nodeValue);
        } else {
            this.children.put(head, new TrieNode<>(tail, nodeValue));
        }
    }

    public int count() {
        int count = 0;
        for (final TrieNode node : this.children.values()) {
            count += node.isTerminal() ? 1 : node.count();
        }
        return count;
    }

    public boolean contains(final String text) {
        return internalContains(text, "");
    }

    public Option<T> get(final String text) {
        return internalGet(text, "");
    }

    public Set<String> startingWith(final String text) {
        final Set<String> results = new HashSet<>();
        internalStartingWith(text, "", results);
        return results;
    }

    public Set<String> toSet() {
        final Set<String> results = new HashSet<>();
        internalToList("", results);
        return results;
    }

    boolean isTerminal() {
        return this.children.isEmpty();
    }

    private boolean internalContains(final String text, final String heading) {
        if (text == null || text.isEmpty()) {
            return isTerminal();
        }
        final Character head = text.charAt(0);
        final String tail = text.substring(1);

        if (this.children.containsKey(head)) {
            return this.children.get(head).internalContains(tail, heading + head);
        } else {
            return heading.equals(text);
        }
    }


    private Option<T> internalGet(final String text, final String heading) {
        if (text == null || text.isEmpty()) {
            return isTerminal() ? this.value : Option.<T>absent();
        }
        final Character head = text.charAt(0);
        final String tail = text.substring(1);

        if (this.children.containsKey(head)) {
            return this.children.get(head).internalGet(tail, heading + head);
        } else {
            return Option.absent();
        }
    }

    private void internalStartingWith(final String text, final String heading, final Set<String> results) {
        if (text == null || text.isEmpty()) {
            internalToList(heading, results);
            return;
        }

        final Character head = text.charAt(0);
        final String tail = text.substring(1);

        if (this.children.containsKey(head)) {
            this.children.get(head).internalStartingWith(tail, heading + head, results);
        } else {
            results.add(heading);
        }
    }

    private void internalToList(final String heading, final Set<String> results) {
        if (isTerminal()) {
            results.add(heading);
            return;
        }
        for (final Map.Entry<Character, TrieNode<T>> entry : this.children.entrySet()) {
            entry.getValue().internalToList(heading + entry.getKey(), results);
        }
    }
}
