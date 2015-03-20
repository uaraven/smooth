package net.ninjacat.smooth.utils;

/**
 * Simple two element tuple combining two elements. Elements are reference to as left and right, however that is
 * solely for convenience, no spacial placing exist inside the pair.
 */
public class Pair<L, R> {

    private final L left;
    private final R right;

    private Pair(final L left, final R right) {

        this.left = left;
        this.right = right;
    }

    /**
     * Creates new Pair of two elements.
     *
     * @param left  Left value.
     * @param right Right value.
     * @param <L>   Type of left value.
     * @param <R>   Type of right value.
     * @return New Pair.
     */
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<L, R>(left, right);
    }

    /**
     * @return left element of the tuple.
     */
    public L getLeft() {
        return this.left;
    }

    /**
     * @return right element of the tuple.
     */
    public R getRight() {
        return this.right;
    }
}
