package net.ninjacat.smooth.utils;

import net.ninjacat.smooth.iterators.Iter;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JoinerTest {

    @Test
    public void testShouldJoinStrings() throws Exception {
        final String joined = Joiner.on("-").join("1", "2", "3");

        assertThat("Should join strings with separator", joined, is("1-2-3"));
    }

    @Test
    public void testShouldJoinNothingIntoEmptyString() throws Exception {
        final String joined = Joiner.on("-").join();

        assertThat("Should join nothing into empty string", joined, is(""));
    }

    @Test
    public void testShouldJoinOnePartIntoItself() throws Exception {
        final String joined = Joiner.on("-").join(1);

        assertThat("Should join one part into itself", joined, is("1"));
    }

    @Test
    public void testShouldJoinStringsWithMultiCharSeparator() throws Exception {
        final String joined = Joiner.on("-!-").join("1", "2", "3");

        assertThat("Should join strings with multicharacter separator", joined, is("1-!-2-!-3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldFailWithNullValue() throws Exception {
        Joiner.on(",").join("1", null, "2", "3");
    }

    @Test
    public void testShouldIgnoreNulls() throws Exception {
        final String joined = Joiner.on("-").skipNulls().join("1", "2", null, "3");

        assertThat("Should join strings ignoring nulls", joined, is("1-2-3"));
    }

    @Test
    public void testShouldReplaceNullsWithDefault() throws Exception {
        final String joined = Joiner.on("-").useForNulls("--").join("1", "2", null, "3");

        assertThat("Should join strings replacing nulls", joined, is("1-2----3"));
    }

    @Test
    public void testShouldIgnoreReplacementIfSkipNulls() throws Exception {
        final String joined = Joiner.on("-").useForNulls("--").skipNulls().join("1", "2", null, "3");

        assertThat("Should not replace nulls if skip nulls configured", joined, is("1-2-3"));
    }


    @Test
    public void testShouldAllowDuplicateSeparators() throws Exception {
        final String joined = Joiner.on("-").join("1-", "2-", "-3");

        assertThat("Should allow duplicate separators", joined, is("1--2---3"));
    }

    @Test
    public void testShouldRemoveDuplicateSeparators() throws Exception {
        final String joined = Joiner.on("-").noDuplicateSeparators().join("1-", "2-", "-3");

        assertThat("Should remove duplicate separators", joined, is("1-2-3"));
    }

    @Test
    public void testShouldRemoveDupSeparatorsAndKeepNonDups() throws Exception {
        final String joined = Joiner.on("-").noDuplicateSeparators().join("-1-", "2", "-3-");

        assertThat("Should remove duplicate separators and leave non-duplicate", joined, is("-1-2-3-"));
    }

    @Test
    public void testShouldJoinIterables() throws Exception {
        final String joined = Joiner.on("-").joinIterable(Iter.of(1, 2, 3));

        assertThat("Should join iterables", joined, is("1-2-3"));
    }

    @Test
    public void testShouldJoinIterableToAppendable() throws Exception {
        final StringBuilder sb = new StringBuilder("Prefix-");
        Joiner.on("-").appendIterableTo(sb, Iter.of(1, 2, 3));

        assertThat("Should append iterable to appendable", sb.toString(), is("Prefix-1-2-3"));
    }

    @Test
    public void testShouldJoinObjectsToAppendable() throws Exception {
        final StringBuilder sb = new StringBuilder("Prefix-");
        Joiner.on("-").appendTo(sb, 1, 2, 3);

        assertThat("Should append objects to appendable", sb.toString(), is("Prefix-1-2-3"));
    }
}