package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

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

    @Before
    public void setUp() {
        cat = new Cat(id, name, owner, age);
    }

    @After
    public void tearDown() {
        cat = null;
    }

    @Test
    public void testGetName() {
        assertEquals(name, cat.getName());
    }

    @Test
    public void testSetName() {
        cat.setName(newName);
        assertEquals(newName, cat.getName());
    }

    @Test
    public void testGetOwner() {
        assertEquals(owner, cat.getOwner());
    }

    @Test
    public void testSetOwner() {
        cat.setOwner(newOwner);
        assertEquals(newOwner, cat.getOwner());
    }

    @Test
    public void testGetAge() {
        assertEquals(age, cat.getCatYears());
    }

    @Test
    public void testSetAge() {
        cat.setCatYears(newAge);
        assertEquals(newAge, cat.getCatYears());
    }

    @Test
    public void testGetHumanYears(){
        Integer expectedHumanYears = age*15;
        assertEquals(expectedHumanYears, cat.getHumanYears());
    }

    @Test
    public void testEqualForDifferentCatsShouldReturnFalse(){
        Cat newCat = new Cat(newId, newName, newOwner, newAge);
        assertNotEquals(cat, newCat);
    }

    @Test
    public void testEqualForSameCatsShouldReturnTrue(){
        Cat newCat = new Cat(id, name, owner, age);
        assertEquals(cat, newCat);
    }

    @Test
    public void testEqualForDifferentClass(){
        assertNotEquals(cat, new BaseEntity<Long>());
    }
}
