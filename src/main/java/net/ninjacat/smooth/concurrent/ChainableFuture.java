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
