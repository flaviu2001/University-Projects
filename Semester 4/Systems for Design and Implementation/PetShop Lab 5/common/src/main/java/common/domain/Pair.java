package common.domain;

import java.util.Objects;

public class Pair<L, R> {
    L left;
    R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "<" + this.left + ", " + this.right + ">";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair && this.left.equals(((Pair<?, ?>) obj).left)&&
                this.right.equals(((Pair<?, ?>) obj).right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }
}
