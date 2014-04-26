package net.ninjacat.smooth.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterable that can host multiple {@link java.lang.Iterable}s and provide an {@link java.util.Iterator}
 * to walk them all.
 */
public class MultiIterable<T> implements Iterable<T> {
    private List<Iterable<T>> collections;

    public MultiIterable() {
        this.collections = new ArrayList<Iterable<T>>();
    }

    public void append(Iterable<T> iterable) {
        collections.add(iterable);
    }

    @Override
    public Iterator<T> iterator() {
        return new MultiIterator();
    }

    private class MultiIterator implements Iterator<T> {

        private Iterator<Iterable<T>> masterIterator;
        private Iterator<T> slaveIterator;

        private boolean noMoreItems;

        private MultiIterator() {
            masterIterator = collections.iterator();
            noMoreItems = !moveToNextCollection();
        }

        private boolean moveToNextCollection() {
            if (masterIterator.hasNext()) {
                slaveIterator = masterIterator.next().iterator();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean hasNext() {
            if (noMoreItems) {
                return false;
            }
            if (slaveIterator.hasNext()) {
                return true;
            } else {
                if (moveToNextCollection()) {
                    return slaveIterator.hasNext();
                } else {
                    noMoreItems = true;
                    return false;
                }
            }
        }

        @Override
        public T next() {
            if (hasNext()) {
                return slaveIterator.next();
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
