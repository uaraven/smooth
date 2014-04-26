package net.ninjacat.smooth.concurrent;

import net.ninjacat.smooth.functions.Func;
import net.ninjacat.smooth.functions.Procedure;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;


class ChainableFuture<E, T> extends Future<E> {

    private final Func<E, T> transform;
    private final Procedure<T> onPrevSuccess = new Procedure<T>() {
        @Override
        public void call(final T t) {
            doIt(new Callable<E>() {
                @Override
                public E call() throws Exception {
                    return transform.apply(t);
                }
            });
        }
    };
    private final Procedure<Throwable> onPrevFailed = new Procedure<Throwable>() {
        @Override
        public void call(final Throwable throwable) {
            doIt(new Callable<E>() {
                @Override
                public E call() throws Exception {
                    throw (Exception) throwable;
                }
            });
        }
    };

    ChainableFuture(Future<T> parent, Func<E, T> transform, ExecutorService executorService) {
        super(executorService);
        this.transform = transform;
        parent.onSuccess(onPrevSuccess);
        parent.onFailure(onPrevFailed);
    }


}
