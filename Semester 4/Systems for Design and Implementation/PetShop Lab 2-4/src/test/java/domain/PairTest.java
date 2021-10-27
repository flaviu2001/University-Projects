package domain;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PairTest {
    private static final Long leftId = 1L;
    private static final Long newLeftId = 2L;
    private static final Long rightId = 3L;
    private static final Long newRightId = 4L;

    private static Pair<Long, Long> pair;

    @BeforeEach
    public void setUp() {
        pair = new Pair<>(leftId, rightId);
    }

    @AfterEach
    public void tearDown() {
        pair = null;
    }

    @Test
    public void testGetLeft() {
        Assertions.assertEquals(leftId, pair.getLeft());
    }

    @Test
    public void testSetLeft() {
        pair.setLeft(newLeftId);
        Assertions.assertEquals(newLeftId, pair.getLeft());
    }

    @Test
    public void testGetRight() {
        Assertions.assertEquals(rightId, pair.getRight());
    }

    @Test
    public void testSetRight() {
        pair.setRight(newRightId);
        Assertions.assertEquals(newRightId, pair.getRight());
    }

    @Test
    public void testEqualForDifferentPairsShouldReturnFalse(){
        Pair<Long, Long> newPair = new Pair<>(newLeftId, newRightId);
        Assertions.assertNotEquals(pair, newPair);
    }

    @Test
    public void testEqualForSamePairsShouldReturnTrue(){
        Pair<Long, Long> newPair = new Pair<>(leftId, rightId);
        Assertions.assertEquals(pair, newPair);
    }

    @Test
    public void testEqualForDifferentPairTypesShouldReturnFalse(){
        //noinspection AssertBetweenInconvertibleTypes
        Assertions.assertNotEquals(pair, new Pair<>(1L, "a"));
    }

    @Test
    public void testEqualForDifferentClassShouldReturnFalse(){
        //noinspection AssertBetweenInconvertibleTypes
        Assertions.assertNotEquals(pair, new BaseEntity<>());
    }
}
