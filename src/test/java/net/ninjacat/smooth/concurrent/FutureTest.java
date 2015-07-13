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

package net.ninjacat.smooth.concurrent;

import net.ninjacat.smooth.functions.Func;
import net.ninjacat.smooth.functions.Procedure;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class FutureTest {

    private final Answer<java.util.concurrent.Future<?>> answer = new Answer<java.util.concurrent.Future<?>>() {
        @Override
        public java.util.concurrent.Future<?> answer(final InvocationOnMock invocationOnMock) throws Throwable {
            final Runnable arg = (Runnable) invocationOnMock.getArguments()[0];
            arg.run();
            return mock(java.util.concurrent.Future.class);
        }
    };

    @Test
    public void shouldCallSuccessAfterDelay() throws Exception {
        final Future<Integer> integerFuture = new Future<Integer>();
        integerFuture.doIt(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });
        final int[] result = new int[1];
        Thread.sleep(100);
        integerFuture.onSuccess(new Procedure<Integer>() {
            @Override
            public void call(final Integer integer) {
                result[0] = integer;
            }
        });

        assertEquals("Should read result from future", 42, result[0]);
    }

    @Test
    public void shouldCallOnSuccessImmediately() throws Exception {
        final int[] result = new int[1];
        final ExecutorService service = getExecutorService();
        final Future<Integer> integerFuture = new Future<Integer>(service);

        integerFuture.onSuccess(new Procedure<Integer>() {
            @Override
            public void call(final Integer integer) {
                result[0] = integer;
            }
        });

        integerFuture.doIt(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 42;
            }
        });

        assertEquals("Should read result from future", 42, result[0]);
    }

    @Test
    public void shouldReportFailureAfterDelay() throws Exception {
        final Future<Integer> integerFuture = new Future<Integer>();
        integerFuture.doIt(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalAccessException("");
            }
        });

        Thread.sleep(100);

        final int[] result = new int[1];
        integerFuture.onSuccess(new Procedure<Integer>() {
            @Override
            public void call(final Integer integer) {
                result[0] = integer;
            }
        });
        assertEquals("Should read result from future", 0, result[0]);
        integerFuture.onFailure(new Procedure<Throwable>() {
            @Override
            public void call(final Throwable throwable) {
                assertTrue("Expecting IllegalAccessException", throwable instanceof IllegalAccessException);
            }
        });
    }

    @Test
    public void shouldReportFailureImmediately() throws Exception {
        final boolean[] failed = new boolean[1];
        final Future<Integer> integerFuture = new Future<Integer>(getExecutorService());
        integerFuture.onFailure(new Procedure<Throwable>() {
            @Override
            public void call(final Throwable throwable) {
                failed[0] = throwable instanceof IllegalAccessException;
            }
        });
        integerFuture.doIt(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalAccessException("");
            }
        });

        assertTrue("Expecting failure to be reported", failed[0]);
    }

    @Test
    public void shouldChainSuccessfulExecution() throws Exception {
        final Func<Integer, Integer> increment = new Func<Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer) {
                return integer + 1;
            }
        };
        final int[] result = new int[1];
        Future.run(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 0;
            }
        }).then(increment).then(increment).onSuccess(new Procedure<Integer>() {
            @Override
            public void call(final Integer integer) {
                result[0] = integer;
            }
        });
        Thread.sleep(100);
        assertEquals("Expecting correct result", 2, result[0]);
    }

    @Test
    public void shouldChainFailedExecution() throws Exception {
        final Func<Integer, Integer> increment = new Func<Integer, Integer>() {
            @Override
            public Integer apply(final Integer integer) {
                throw new IllegalStateException("");
            }
        };
        final int[] result = new int[1];
        Future.run(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 0;
            }
        }).then(increment).then(increment)
                .onSuccess(new Procedure<Integer>() {
                    @Override
                    public void call(final Integer integer) {
                        result[0] = integer;
                    }
                })
                .onFailure(new Procedure<Throwable>() {
                    @Override
                    public void call(final Throwable throwable) {
                        result[0] = -1;
                    }
                });
        Thread.sleep(100);
        assertEquals("Expecting failure to be propagated and reported", -1, result[0]);
    }

    private ExecutorService getExecutorService() {
        final ExecutorService service = mock(ExecutorService.class);
        doAnswer(answer).when(service).submit(any(Runnable.class));
        return service;
    }

}
