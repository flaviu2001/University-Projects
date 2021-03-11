package domain;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CatTest {
    private static final Long id = 1L;
    private static final Long newId = 2L;
    private static final String name = "cat1";
    private static final String newName = "cat2";
    private static final String owner = "o1";
    private static final String newOwner = "o2";
    private static final Integer age = 5;
    private static final Integer newAge = 10;

    private Cat cat;

    @BeforeEach
    public void setUp() {
        cat = new Cat(id, name, owner, age);
    }

    @AfterEach
    public void tearDown() {
        cat = null;
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(name, cat.getName());
    }

    @Test
    public void testSetName() {
        cat.setName(newName);
        Assertions.assertEquals(newName, cat.getName());
    }

    @Test
    public void testGetOwner() {
        Assertions.assertEquals(owner, cat.getBreed());
    }

    @Test
    public void testSetOwner() {
        cat.setBreed(newOwner);
        Assertions.assertEquals(newOwner, cat.getBreed());
    }

    @Test
    public void testGetAge() {
        Assertions.assertEquals(age, cat.getCatYears());
    }

    @Test
    public void testSetAge() {
        cat.setCatYears(newAge);
        Assertions.assertEquals(newAge, cat.getCatYears());
    }

    @Test
    public void testGetHumanYears(){
        Integer expectedHumanYears = age*15;
        Assertions.assertEquals(expectedHumanYears, cat.getHumanYears());
    }

    @Test
    public void testEqualForDifferentCatsShouldReturnFalse(){
        Cat newCat = new Cat(newId, newName, newOwner, newAge);
        Assertions.assertNotEquals(cat, newCat);
    }

    @Test
    public void testEqualForSameCatsShouldReturnTrue(){
        Cat newCat = new Cat(id, name, owner, age);
        Assertions.assertEquals(cat, newCat);
    }

    @Test
    public void testEqualForDifferentClass(){
        Assertions.assertNotEquals(cat, new BaseEntity<Long>());
    }
}
