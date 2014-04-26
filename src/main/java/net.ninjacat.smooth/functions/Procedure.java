package net.ninjacat.smooth.functions;

/**
 * Function of one parameter without result. Used for side-effects. Essentially a {@link Runnable} with a parameter
 */
public abstract class Procedure<T> implements Func<Void, T> {

    @Override
    public Void apply(T t) {
        call(t);
        return null;
    }

    public abstract void call(T t);
}
