package net.ninjacat.smooth.concurrent;

import net.ninjacat.smooth.functions.F;
import net.ninjacat.smooth.functions.Procedure;
import net.ninjacat.smooth.utils.Try;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 26/04/14.
 */
public class Future<E> {
    private static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private final ExecutorService executor;
    private volatile Procedure<E> successHandler;
    private volatile Procedure<Throwable> failHandler;
    private volatile Try<E> result;

    public Future() {
        this(null);
    }

    public Future(ExecutorService executor) {
        this.executor = executor == null ? DEFAULT_EXECUTOR_SERVICE : executor;
        result = null;
    }

    public static <E> Future<E> run(Callable<E> block) {
        return new Future<E>(null).doIt(block);
    }

    public <T> Future<T> then(F<T, E> transform) {
        return new ChainableFuture<T, E>(this, transform);
    }

    public Future<E> onFailure(Procedure<Throwable> onFailure) {
        if (result != null && !result.isSuccessful()) {
            onFailure.call(result.getFailure());
        } else {
            this.failHandler = onFailure;
        }
        return this;
    }

    public Future<E> onSuccess(Procedure<E> onSuccess) {
        if (result != null && result.isSuccessful()) {
            onSuccess.call(result.getValue());
        } else {
            this.successHandler = onSuccess;
        }
        return this;
    }

    public Future<E> doIt(final Callable<E> callable) {
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
