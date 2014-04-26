package net.ninjacat.smooth.functions;

public abstract class F2<R, P1, P2> {
    public abstract R apply(P1 p1, P2 p2);

    public F<R, P2> partialApply(final P1 p1) {
        return new F<R, P2>() {
            public R apply(P2 p2) {
                return F2.this.apply(p1, p2);
            }
        };
    }
}
