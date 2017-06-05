package net.ninjacat.smooth.collections;

import java.util.Collection;

/**
 * Array manipulation utilities.
 */
public final class Arrays {

    private Arrays() {
        super();
    }

    /**
     * Converts collection of {@link Integer}s to array of primitives
     *
     * @param ints Collection of Integer objects
     * @return array of ints
     */
    public static int[] toIntArray(final Collection<Integer> ints) {
        final int[] results = new int[ints.size()];
        int index = 0;
        for (final Integer intValue : ints) {
            results[index] = intValue;
            index += 1;
        }
        return results;
    }

    /**
     * Converts array of primitive ints into array of object {@link Integer}s
     *
     * @param ints array of ints
     * @return array of Integers
     */
    public static Integer[] boxIntArray(final int[] ints) {
        final Integer[] results = new Integer[ints.length];
        for (int i = 0; i < ints.length; i++) {
            results[i] = ints[i];
        }
        return results;
    }

    /**
     * Converts collection of {@link Double}s to array of primitives
     *
     * @param doubles Collection of Integer objects
     * @return array of doubles
     */
    public static double[] toDoubleArray(final Collection<Double> doubles) {
        final double[] results = new double[doubles.size()];
        int index = 0;
        for (final Double intValue : doubles) {
            results[index] = intValue;
            index += 1;
        }
        return results;
    }

    /**
     * Converts array of primitive doubles into array of object {@link Double}s
     *
     * @param doubles array of doubles
     * @return array of Integers
     */
    public static Double[] boxDoubleArray(final double[] doubles) {
        final Double[] results = new Double[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            results[i] = doubles[i];
        }
        return results;
    }
}
