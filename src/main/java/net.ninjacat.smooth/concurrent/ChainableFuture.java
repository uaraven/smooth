package net.ninjacat.smooth.concurrent;

import net.ninjacat.smooth.functions.F;
import net.ninjacat.smooth.functions.Procedure;

import java.util.concurrent.Callable;

/**
 * Created on 26/04/14.
 */
public class ChainableFuture<E, T> extends Future<E> {

    private final F<E, T> transform;
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
                    if (throwable instanceof Exception) {
                        throw (Exception)throwable;
                    }else{
                        throw new Exception(throwable);
                    }
                }
            });
        }
    };

    ChainableFuture(Future<T> parent, F<E, T> transform) {
        super(null);
        this.transform = transform;
        parent.onSuccess(onPrevSuccess);
        parent.onFailure(onPrevFailed);
    }


}
