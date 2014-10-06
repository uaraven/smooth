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

/**
 * Simple implementation of two-argument function with an implementation of partial application
 */
public abstract class Function2<R, P1, P2> implements Func2<R, P1, P2> {
    public abstract R apply(P1 p1, P2 p2);

    public Func<R, P2> partialApply(final P1 p1) {
        return new Func<R, P2>() {
            public R apply(P2 p2) {
                return Function2.this.apply(p1, p2);
            }
        };
    }
}
