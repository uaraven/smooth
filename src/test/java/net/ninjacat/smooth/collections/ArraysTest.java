package net.ninjacat.smooth.collections;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class ArraysTest {

    @Test
    public void shouldBoxPrimitiveIntArray() throws Exception {
        final int[] data = new int[]{1, 2, 3};
        final Integer[] integers = Arrays.boxIntArray(data);

        assertThat(integers, Matchers.equalTo(new Integer[]{1, 2, 3}));
    }


    @Test
    public void shouldBoxPrimitiveDoubleArray() throws Exception {
        final double[] data = new double[]{1.3, 2.2, 3.1};
        final Double[] doubles = Arrays.boxDoubleArray(data);

        assertThat(doubles, Matchers.equalTo(new Double[]{1.3, 2.2, 3.1}));
    }


    @Test
    public void shouldUnboxIntegerCollection() throws Exception {
        final List<Integer> data = Lists.of(1, 2, 3);
        final int[] integers = Arrays.toIntArray(data);

        assertThat(integers, Matchers.equalTo(new int[]{1, 2, 3}));
    }


    @Test
    public void shouldUnboxDoubleCollection() throws Exception {
        final List<Double> data = Lists.of(1.1, 2.2, 3.1);
        final double[] integers = Arrays.toDoubleArray(data);

        assertThat(integers, Matchers.equalTo(new double[]{1.1, 2.2, 3.1}));
    }
}