/*
 * Copyright 2014 Oleksiy Voronin <ovoronin@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.collections.Collect;
import net.ninjacat.smooth.functions.*;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.*;

import static net.ninjacat.smooth.iterators.IterFixtures.SideEffect;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class IterTest {

    @Test
    public void toListShouldReturnOriginalList() throws Exception {
        final List<String> original = Arrays.asList("1", "1", "Last");
        final List<String> result = Iter.of(original).toList();

        assertThat(result, is(original));
    }

    @Test
    public void toSetShouldReturnOnlyUniqueElements() throws Exception {
        final List<String> original = Arrays.asList("1", "1", "Last");
        final Set<String> result = Iter.of(original).toSet();

        assertThat(result, CoreMatchers.is(Collect.setOf("1", "Last")));
    }

    @Test
    public void mapShouldTransformValues() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));
        final Iterable<Integer> transformed = iter.map(new Func<Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer) {
                return integer * 2;
            }
        });
        final Iterator<Integer> iterator = transformed.iterator();
        IterFixtures.verifyNext(iterator, 2);
        IterFixtures.verifyNext(iterator, 4);
        IterFixtures.verifyNext(iterator, 6);
        IterFixtures.verifyNoNext(iterator);
    }

    @Test
    public void mapShouldBeLazy() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));

        final IterFixtures.SideEffect sideEffect = new IterFixtures.SideEffect();

        iter.map(new Func<Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer) {
                sideEffect.sideEffect();
                return integer * 2;
            }
        });
        assertThat(sideEffect.hasSideEffects(), is(false));
    }

    @Test
    public void filterShouldReturnOddValues() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));
        final Iterable<Integer> filtered = iter.filter(new Predicate<Integer>() {
            @Override
            public boolean matches(final Integer integer) {
                return 0 != integer % 2;
            }
        });
        final Iterator<Integer> iterator = filtered.iterator();
        IterFixtures.verifyNext(iterator, 1);
        IterFixtures.verifyNext(iterator, 3);
        IterFixtures.verifyNoNext(iterator);
    }

    @Test
    public void filterShouldBeLazy() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));

        final IterFixtures.SideEffect sideEffect = new IterFixtures.SideEffect();

        iter.filter(new Predicate<Integer>() {
            @Override
            public boolean matches(final Integer integer) {
                sideEffect.sideEffect();
                return 0 != integer % 2;
            }
        });

        assertThat(sideEffect.hasSideEffects(), is(false));
    }


    @Test
    public void reduceShouldFoldLeft() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        final int result = iter.reduce(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer, final Integer integer2) {
                return integer + integer2;
            }
        });

        assertThat(result, is(10));
    }

    @Test
    public void lazyReduceShouldFoldLeft() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        final Promise<Integer> result = iter.lazyReduce(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer, final Integer integer2) {
                return integer + integer2;
            }
        });

        assertThat(result.get(), is(10));
    }

    @Test
    public void lazyReduceShouldBeLazy() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        final IterFixtures.SideEffect sideEffect = new IterFixtures.SideEffect();

        iter.lazyReduce(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer, final Integer integer2) {
                sideEffect.sideEffect();
                return integer + integer2;
            }
        });

        assertThat(sideEffect.hasSideEffects(), is(false));
    }

    @Test
    public void forEachShouldIterateOverAllElements() throws Exception {
        final Iter<Integer> iter = Iter.of(1, 2, 3, 4);

        final int[] sum = {0};
        iter.forEach(new Procedure<Integer>() {
            @Override
            public void call(final Integer integer) {
                sum[0] = sum[0] + integer;
            }
        });

        assertThat(sum[0], is(10));
    }

    @Test
    public void findShouldBeAbleToLocateElement() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        final Integer result = iter.find(new Predicate<Integer>() {
            @Override
            public boolean matches(final Integer integer) {
                return integer.equals(3);
            }
        }, -1);

        assertThat(result, is(3));
    }

    @Test
    public void findShouldReturnDefaultValueIfElementNotFound() throws Exception {
        final Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        final Integer result = iter.find(new Predicate<Integer>() {
            @Override
            public boolean matches(final Integer integer) {
                return integer.equals(5);
            }
        }, -1);

        assertThat(result, is(-1));
    }

    @Test
    public void lazyFindShouldBeLazy() throws Exception {
        final Iter<Integer> iter = Iter.of(1, 2);

        final SideEffect sideEffect = new SideEffect();

        iter.lazyFind(new Predicate<Integer>() {
            @Override
            public boolean matches(final Integer integer) {
                sideEffect.sideEffect();
                return integer.equals(3);
            }
        }, -1);

        assertThat(sideEffect.hasSideEffects(), is(false));
    }

    @Test
    public void shouldFindDataWhenDoingLazySearch() throws Exception {
        final Iter<Integer> iter = Iter.of(1, 2, 3, 4);

        final SideEffect sideEffect = new SideEffect();

        final Integer result = iter.lazyFind(new Predicate<Integer>() {
            @Override
            public boolean matches(final Integer integer) {
                sideEffect.sideEffect();
                return integer.equals(3);
            }
        }, -1).get();

        assertThat(sideEffect.hasSideEffects(), is(true));
        assertThat(result, is(3));
    }

    @Test
    public void anyShouldReturnTrueIfAnyMatchingElementFound() throws Exception {
        final Iter<String> iter = Iter.of("First", "Middle", "Last");

        final boolean result = iter.any(new Predicate<String>() {
            @Override
            public boolean matches(final String o) {
                return o.startsWith("M");
            }
        });

        assertThat(result, is(true));
    }

    @Test
    public void anyShouldReturnFalseIfNoMatchingElementFound() throws Exception {
        final Iter<String> iter = Iter.of("First", "Middle", "Last");

        final boolean result = iter.any(new Predicate<String>() {
            @Override
            public boolean matches(final String o) {
                return o.startsWith("S");
            }
        });

        assertThat(result, is(false));
    }

    @Test
    public void allShouldReturnTrueIfAllElementsMatchPredicate() throws Exception {
        final Iter<String> iter = Iter.of("Mary", "Molly", "Maria");

        final boolean result = iter.all(new Predicate<String>() {
            @Override
            public boolean matches(final String o) {
                return o.startsWith("M");
            }
        });

        assertThat(result, is(true));
    }

    @Test
    public void allShouldReturnFalseIfAtLeastOneElementDoesNotMatchPredicate() throws Exception {
        final Iter<String> iter = Iter.of("Mary", "Molly", "Jenny");

        final boolean result = iter.all(new Predicate<String>() {
            @Override
            public boolean matches(final String o) {
                return o.startsWith("M");
            }
        });

        assertThat(result, is(false));
    }

    @Test
    public void shouldCreateStringWithDelimiters() throws Exception {
        final Iter<Integer> iter = Iter.of(1, 2, 3);

        final String result = iter.mkStr("->");

        assertThat("Should create string with separators", result, is("1->2->3"));
    }

    @Test
    public void shouldCollectElements() throws Exception {
        final Iter<Integer> iter = Iter.of(1, 2, 3);

        final Collection<Integer> collection = iter.collectWith(Collectors.<Integer>arrayList());

        assertThat("should contain elements from iterable", collection, hasItems(1, 2, 3));
    }


    @Test
    public void shouldConvertIterableToArray() throws Exception {
        final Iter<Integer> iter = Iter.of(1, 2);

        final Integer[] array = iter.toArray(Integer.class);

        assertThat("should create array of correct size", array.length, is(2));
        assertThat("array should contain elements from iterable", array, is(new Integer[]{1, 2}));
    }

    @Test
    public void shouldConvertIterToMap() throws Exception {
        final Iter<String> iter = Iter.of("1", "2");

        final Map<Integer, String> map = iter.toMap(new Func<Integer, String>() {
            @Override
            public Integer apply(final String s) {
                return Integer.parseInt(s);
            }
        });

        assertThat("Generated map should contain pair 1->'1'", map.get(1), is("1"));
        assertThat("Generated map should contain pair 2->'2'", map.get(2), is("2"));
        assertThat("Generated map should contain two elements", map.size(), is(2));
    }
}
