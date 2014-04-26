package net.ninjacat.smooth.functions;

/**
 * Nullary function. A promise to return some value later.
 *
 * @param <R> Type of value to return
 */
public interface Promise<R> {

    /**
     * Call this method to evaluate promise and get promised result
     *
     * @return result of the calculation
     */
    R get();
}
