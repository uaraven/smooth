package net.ninjacat.smooth.concurrent;

import net.ninjacat.smooth.functions.Procedure;
import net.ninjacat.smooth.functions.Func;

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
import static org.mockito.Mockito.when;

/**
 * Created on 26/04/14.
 */
public class FutureTest {

    private final Answer<java.util.concurrent.Future<?>> answer = new Answer<java.util.concurrent.Future<?>>() {
        @Override
        public java.util.concurrent.Future<?> answer(InvocationOnMock invocationOnMock) throws Throwable {
            Runnable arg = (Runnable) invocationOnMock.getArguments()[0];
            arg.run();
            return mock(java.util.concurrent.Future.class);
        }
    };

    @Test
    public void shouldCallSuccessAfterDelay() throws Exception {
        Future<Integer> integerFuture = new Future<Integer>();
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
            public void call(Integer integer) {
                result[0] = integer;
            }
        });

        assertEquals("Should read result from future", 42, result[0]);
    }

    @Test
    public void shouldCallOnSuccessImmediately() throws Exception {
        final int[] result = new int[1];
        ExecutorService service = getExecutorService();
        Future<Integer> integerFuture = new Future<Integer>(service);

        integerFuture.onSuccess(new net.ninjacat.smooth.functions.Procedure<Integer>() {
            @Override
            public void call(Integer integer) {
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
        Future<Integer> integerFuture = new Future<Integer>();
        integerFuture.doIt(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalAccessException();
            }
        });
        final int[] result = new int[1];

        Thread.sleep(100);

        integerFuture.onSuccess(new Procedure<Integer>() {
            @Override
            public void call(Integer integer) {
                result[0] = integer;
            }
        });
        assertEquals("Should read result from future", 0, result[0]);
        integerFuture.onFailure(new Procedure<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                assertTrue(throwable instanceof IllegalAccessException);
            }
        });
    }

    @Test
    public void shouldReportFailureImmediately() throws Exception {
        final boolean[] failed = new boolean[1];
        Future<Integer> integerFuture = new Future<Integer>(getExecutorService());
        integerFuture.onFailure(new Procedure<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                failed[0] = throwable instanceof IllegalAccessException;
            }
        });
        integerFuture.doIt(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new IllegalAccessException();
            }
        });

        assertTrue(failed[0]);
    }

    @Test
    public void shouldChainSuccessfulExecution() throws Exception {
        Func<Integer, Integer> increment = new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
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
            public void call(Integer integer) {
                result[0] = integer;
            }
        });
        Thread.sleep(100);
        assertEquals(2, result[0]);
    }

    @Test
    public void shouldChainFailedExecution() throws Exception {
        Func<Integer, Integer> increment = new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                throw new IllegalStateException();
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
                    public void call(Integer integer) {
                        result[0] = integer;
                    }
                })
                .onFailure(new Procedure<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        result[0] = -1;
                    }
                });
        Thread.sleep(100);
        assertEquals(-1, result[0]);
    }

    private ExecutorService getExecutorService() {
        ExecutorService service = mock(ExecutorService.class);
        doAnswer(answer).when(service).submit(any(Runnable.class));
        return service;
    }

}
