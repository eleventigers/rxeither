package net.jokubasdargis.rxeither;

import rx.functions.Action1;
import rx.functions.Func1;

final class Right<L, R> extends Either<L, R> {

    private final R value;

    Right(R value) {
        this.value = value;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public void continued(Action1<L> left, Action1<R> right) {
        right.call(value);
    }

    @Override
    public <R1> R1 join(Func1<L, R1> left, Func1<R, R1> right) {
        return right.call(value);
    }

    @Deprecated
    @Override
    public void fold(Action1<L> left, Action1<R> right) {
        continued(left, right);
    }

    @Deprecated
    @Override
    public <T> T fold(Func1<L, T> left, Func1<R, T> right) {
        return join(left, right);
    }

    @Override
    public String toString() {
        return "Right{" + "value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Right) {
            Right<?, ?> that = (Right<?, ?>) o;
            return (this.value.equals(that.value));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.value.hashCode();
        return h;
    }
}
