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

package net.ninjacat.smooth.functions;

import java.util.concurrent.Callable;

/**
 * Function of one parameter without result. Used for side-effects. Essentially a {@link Runnable} with a parameter
 */
public abstract class Procedure<T> implements Func<Void, T> {

    /**
     * Override this method to implement procedure.
     *
     * @param t Parameter to procedure.
     */
    public abstract void call(T t);

    @Override
    public final Void apply(final T t) {
        call(t);
        return null;
    }

    /**
     * Converts Procedure to a {@link Callable}. As Procedure receives a parameter while Callable does not,
     * it is required to specify this parameter at the conversion moment.
     *
     * @param t Parameter for this procedure.
     * @return Callable.
     */
    public Callable<Void> asCallable(final T t) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Procedure.this.call(t);
                return null;
            }
        };
    }

}
