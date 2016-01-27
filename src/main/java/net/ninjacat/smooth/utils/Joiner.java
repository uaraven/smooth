package net.ninjacat.smooth.utils;

import net.ninjacat.smooth.iterators.Iter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Object that joins parts of text specified as iterable, varargs or array.
 * <p/>
 * If neither skipNulls() nor useForNull(String) is specified, the joining methods will throw IllegalArgumentException
 * if any given element is null.
 */
public class Joiner {

    private final String separator;
    private final String nullValue;
    private final boolean skipNulls;
    private final boolean duplicateSeparators;
    /**
     * Ready to use Joiner for paths
     */
    public static final Joiner PATH_JOINER = Joiner.on(File.separator).skipNulls().noDuplicateSeparators();

    private Joiner(final String separator,
                   final String nullValue,
                   final boolean skipNulls,
                   final boolean duplicateSeparators) {
        this.separator = separator;
        this.nullValue = nullValue;
        this.skipNulls = skipNulls;
        this.duplicateSeparators = duplicateSeparators;
    }

    /**
     * Creates a new Joiner object with a specified separator. New Joiner will fail on {@code null} values in input
     * and will put separators even if they are already present
     *
     * @param separator Separator to use for text concatenation.
     * @return new Joiner object.
     */
    @SuppressWarnings("StaticMethodNamingConvention")
    public static Joiner on(final String separator) {
        return new Joiner(separator, null, false, true);
    }

    /**
     * Sets the string to use in place of {@code null} elements. If Joiner is set to skip nulls this method does
     * nothing.
     *
     * @param forNulls String to substitute {@code null}s with.
     * @return Updated Joiner
     */
    public Joiner useForNulls(final String forNulls) {
        return this.skipNulls ? this : new Joiner(this.separator, forNulls, false, this.duplicateSeparators);
    }

    /**
     * Configures Joiner to ignore {@code null} elements. If {@link #useForNulls(String)} has been configured, it will
     * be ignored from now on.
     *
     * @return Updated Joiner
     */
    public Joiner skipNulls() {
        return new Joiner(this.separator, this.nullValue, true, this.duplicateSeparators);
    }

    /**
     * Configures Joiner to prevent duplicate separators. This is useful when constructing file system paths.
     * <p/>
     * For example
     * <p/>
     * {@code
     * Joiner.on("/").join("/", "somepath", "otherpath/", "/filename");
     * }
     * <p/>
     * will return {@code "//somepath/otherpath///filename"} and
     * <p/>
     * {@code
     * Joiner.on("/").noDuplicateSeparators().join("/", "somepath", "otherpath/", "/filename");
     * }
     * <p/>
     * will return {@code "/somepath/otherpath/filename"}
     *
     * @return Updated Joiner
     */
    public Joiner noDuplicateSeparators() {
        return new Joiner(this.separator, this.nullValue, this.skipNulls, false);
    }

    /**
     * Joins parts and returns them as a string. {@link Object#toString()} is called on each part before joining.
     *
     * @param parts Parts of string to be joined.
     * @return Joined string
     * @throws IllegalArgumentException if parts contain {@code null} and neither {@link #skipNulls()} nor
     *                                  {@link #useForNulls(String)} are used.
     */
    public String join(final Object... parts) {
        final StringBuilder builder = new StringBuilder();
        try {
            appendIterableTo(builder, Iter.of(parts));
        } catch (final IOException ignored) {
        }
        return builder.toString();
    }

    /**
     * Joins parts and returns them as a string. {@link Object#toString()} is called on each part before joining.
     *
     * @param parts Parts of string to be joined.
     * @return Joined string
     * @throws IllegalArgumentException if parts contain {@code null} and neither {@link #skipNulls()} nor
     *                                  {@link #useForNulls(String)} are used.
     */
    public String joinIterable(final Iterable<?> parts) {
        final StringBuilder builder = new StringBuilder();
        try {
            appendIterableTo(builder, parts);
        } catch (final IOException ignored) {
        }
        return builder.toString();
    }


    /**
     * Appends parts to a given {@link Appendable}. {@link Object#toString()} is called on each part before joining.
     *
     * @param appendable {@link Appendable} to append parts to
     * @param parts      Parts to be joined
     * @param <A>        Type of Appendable
     * @throws IOException              Thrown by Appendable
     * @throws IllegalArgumentException if parts contain {@code null} and neither {@link #skipNulls()} nor
     *                                  {@link #useForNulls(String)} are used.
     */
    public <A extends Appendable> void appendTo(final A appendable, final Object... parts) throws IOException {
        appendIterableTo(appendable, Iter.of(parts));
    }

    /**
     * Appends parts of {@link Iterable} to a given {@link Appendable}
     *
     * @param appendable {@link Appendable} to append parts to
     * @param parts      Iterable of parts
     * @param <A>        Type of Appendable
     * @throws IOException              Thrown by Appendable
     * @throws IllegalArgumentException if parts contain {@code null} and neither {@link #skipNulls()} nor
     *                                  {@link #useForNulls(String)} are used.
     */
    public <A extends Appendable> void appendIterableTo(final A appendable, final Iterable<?> parts) throws IOException {
        boolean notFirstPart = false;
        final Iterator<?> iterator = parts.iterator();
        while (iterator.hasNext()) {
            final Object part = iterator.next();
            final String partValue;
            if (null == part) {
                if (this.skipNulls) {
                    continue;
                } else if (null != this.nullValue) {
                    partValue = this.nullValue;
                } else {
                    throw new IllegalArgumentException("Cannot join null parts");
                }
            } else {
                partValue = part.toString();
            }
            final boolean hasNext = iterator.hasNext();
            if (this.duplicateSeparators) {
                appendable.append(partValue);
                if (hasNext) {
                    appendable.append(this.separator);
                }
            } else {
                String updatedPart = partValue;
                if (notFirstPart && partValue.startsWith(this.separator)) {
                    updatedPart = updatedPart.substring(this.separator.length());
                }
                if (partValue.endsWith(this.separator) && hasNext) {
                    updatedPart = updatedPart.substring(0, updatedPart.length() - this.separator.length());
                }
                appendable.append(updatedPart);
                if (hasNext) {
                    appendable.append(this.separator);
                }
            }
            notFirstPart = true;
        }
    }

}
