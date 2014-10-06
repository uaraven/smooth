package net.ninjacat.smooth.concurrent;

import net.ninjacat.smooth.functions.Func;
import net.ninjacat.smooth.functions.Procedure;
import net.ninjacat.smooth.utils.Try;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * Represents long-running operation that should be executed asynchronously.
 * </p>
 * <p>
 * Result of the operation is either a value or a {@link java.lang.Throwable}. To get the result of operation
 * caller should supply handlers for success and/or failure.
 * </p>
 * <p>
 * Several operations can be chained by using {@link #then(net.ninjacat.smooth.functions.Func)} method, for
 * example:
 * <pre>
 *     Future.run(callable).then(transform1).then(transform2).onSuccess(ok).onFailure(boom);
 * </pre>
 * This operation can fail at any time, but will never allow any exception escape. In the event that every
 * callable and transform completes successfully then {@link #onSuccess(net.ninjacat.smooth.functions.Procedure)}
 * will be called, otherwise {@link #onFailure(net.ninjacat.smooth.functions.Procedure)} will be called with
 * and exception as parameter. All the operations will be performed asynchronously.
 * </p>
 */
public class Future<E> {
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private final ExecutorService executor;
    private volatile Procedure<E> successHandler;
    private volatile Procedure<Throwable> failHandler;
    private volatile Try<E> result;
    private volatile boolean executedOnce;

    /**
     * Creates a future with a default executor
     */
    public Future() {
        this(null);
    }

    /**
     * Creates a future with supplied executor service
     *
     * @param executor {@link java.util.concurrent.ExecutorService} to be used for asynchronous execution
     */
    public Future(ExecutorService executor) {
        this.executor = executor == null ? DEFAULT_EXECUTOR_SERVICE : executor;
        result = null;
    }

    /**
     * <p>
     * Shortcut for executing a {@link java.util.concurrent.Callable} with default parameters.
     * </p>
     * <p>
     * Will create a new Future and execute it.
     * </p>
     *
     * @param block callable to be executed
     * @param <E>   type of the result
     * @return Future&lt;E&gt;
     */
    public static <E> Future<E> run(Callable<E> block) {
        return new Future<E>().doIt(block);
    }

    /**
     * <p>
     * Allows chaining asynchronous operations.
     * </p>
     * <p>
     * Will start new supplied transform function within a new Future as soon as this future succeeds. In
     * case this future fails, failure cause will be propagated to the new Future.
     * </p>
     *
     * @param transform function to transform results of this future into result of a new future
     * @param <T>       type of the new future result
     * @return Future wrapping transform function
     */
    public final <T> Future<T> then(Func<T, E> transform) {
        return new ChainableFuture<T, E>(this, transform, executor);
    }

    /**
     * <p>Sets successful execution handler.</p>
     * <p>This handler can be set only once. Will throw {@link java.lang.IllegalStateException} if attempt to set
     * it again will be made</p>
     * <p>There are no guarantees when the callback will be called</p>
     *
     * @param onSuccess {@link net.ninjacat.smooth.functions.Procedure} to handle successful Future execution
     * @return this future
     */
    public final Future<E> onSuccess(Procedure<E> onSuccess) {
        if (successHandler != null) {
            throw new IllegalStateException("Cannot reassign onSuccess handler");
        }
        if (result != null && result.isSuccessful()) {
            onSuccess.call(result.getValue());
        } else {
            this.successHandler = onSuccess;
        }
        return this;
    }

    /**
     * <p>Sets failed execution handler.</p>
     * <p>This handler can be set only once. Will throw {@link java.lang.IllegalStateException} if attempt to set
     * it again will be made</p>
     * <p>There are no guarantees when the callback will be called</p>
     *
     * @param onFailure {@link net.ninjacat.smooth.functions.Procedure} to handle failed Future execution
     * @return this future
     */
    public final Future<E> onFailure(Procedure<Throwable> onFailure) {
        if (failHandler != null) {
            throw new IllegalStateException("Cannot reassign onFailure handler");
        }
        if (result != null && !result.isSuccessful()) {
            onFailure.call(result.getFailure());
        } else {
            this.failHandler = onFailure;
        }
        return this;
    }

    /**
     * <p>
     * Executes block of code in the future.
     * </p>
     * <p>
     * Each future can execute code only once. Second attempt ot call this method will throw
     * {@link java.lang.IllegalStateException}
     * </p>
     *
     * @param callable {@link java.util.concurrent.Callable} that will be executed asynchronously
     * @return this future
     */
    public final Future<E> doIt(final Callable<E> callable) {
        if (executedOnce) {
            throw new IllegalStateException();
        } else {
            executedOnce = true;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    result = Try.execute(callable);
                    if (result.isSuccessful()) {
                        reportSuccess(result.getValue());
                    } else {
                        reportFailure(result.getFailure());
                    }
                }
            });
        }
        return this;
    }

    private void reportSuccess(E value) {
        if (successHandler != null) {
            successHandler.call(value);
        }
    }

    private void reportFailure(Throwable fail) {
        if (failHandler != null) {
            failHandler.call(fail);
        }
    }

}
