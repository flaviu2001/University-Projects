package common.domain;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ComparablePair<L extends Comparable<L>, R extends Comparable<R>> extends Pair<L, R> implements Comparable<Pair<L, R>> {

    public ComparablePair(L left, R right) {
        super(left, right);
    }

    @Override
    public int compareTo(Pair<L, R> lrPair) {
        AtomicInteger toReturn = new AtomicInteger(0);
        Stream.of(left.compareTo(lrPair.left), right.compareTo(lrPair.right))
                .filter(i -> i != 0)
                .findAny()
                .ifPresent(toReturn::set);
        return toReturn.get();
    }
}
