package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PairTest {
    private static final Long leftId = 1L;
    private static final Long newLeftId = 2L;
    private static final Long rightId = 3L;
    private static final Long newRightId = 4L;

    private static Pair<Long, Long> pair;

    @Before
    public void setUp() {
        pair = new Pair<>(leftId, rightId);
    }

    @After
    public void tearDown() {
        pair = null;
    }

    @Test
    public void testGetLeft() {
        assertEquals(leftId, pair.getLeft());
    }

    @Test
    public void testSetLeft() {
        pair.setLeft(newLeftId);
        assertEquals(newLeftId, pair.getLeft());
    }

    @Test
    public void testGetRight() {
        assertEquals(rightId, pair.getRight());
    }

    @Test
    public void testSetRight() {
        pair.setRight(newRightId);
        assertEquals(newRightId, pair.getRight());
    }

    @Test
    public void testEqualForDifferentPairsShouldReturnFalse(){
        Pair<Long, Long> newPair = new Pair<>(newLeftId, newRightId);
        assertNotEquals(pair, newPair);
    }

    @Test
    public void testEqualForSamePairsShouldReturnTrue(){
        Pair<Long, Long> newPair = new Pair<>(leftId, rightId);
        assertEquals(pair, newPair);
    }

    @Test
    public void testEqualForDifferentPairTypesShouldReturnFalse(){
        assertNotEquals(pair, new Pair<>(1L, "a"));
    }

    @Test
    public void testEqualForDifferentClassShouldReturnFalse(){
        assertNotEquals(pair, new BaseEntity<>());
    }
}
