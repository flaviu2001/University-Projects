package repository;

import domain.Cat;
import domain.validators.CatValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InMemoryRepositoryTest {
    private static final Cat cat = new Cat(1L, "name", "owner", 2);
    private static final Cat newCat = new Cat(1L, "newName", "newOwner", 3);

    private InMemoryRepository<Long, Cat> repository;

    @Before
    public void setUp(){
        repository = new InMemoryRepository<>(new CatValidator());
    }

    @After
    public void tearDown(){
        repository = null;
    }

    @Test
    public void testSaveValidCat(){
        Optional<Cat> catOptional = repository.save(cat);
        assertTrue(catOptional.isEmpty());
    }

    @Test
    public void testSaveExistentCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.save(cat);
        assertTrue(catOptional.isPresent());
    }

    @Test
    public void testSaveNullCatThrowsError(){
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
        assertEquals("id must not be null", exception.getMessage());
    }

    @Test
    public void testFindOneNullIdThrowsException(){
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findOne(null));
        assertEquals("id must not be null", exception.getMessage());
    }

    @Test
    public void testFindOneExistentId(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.findOne(1L);
        assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(cat.getId()) &&
                catOptional.get().getName().equals(cat.getName()) &&
                catOptional.get().getOwner().equals(cat.getOwner()) &&
                catOptional.get().getHumanYears().equals(cat.getHumanYears())

        );
    }

    @Test
    public void testDeleteNullCatThrowsError(){
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.delete(null));
        assertEquals("id must not be null", exception.getMessage());
    }

    @Test
    public void testDeleteValidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.delete(1L);
        assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(cat.getId()) &&
                catOptional.get().getName().equals(cat.getName()) &&
                catOptional.get().getOwner().equals(cat.getOwner()) &&
                catOptional.get().getHumanYears().equals(cat.getHumanYears()) &&
                StreamSupport.stream(repository.findAll().spliterator(), false).count() == 0
        );
    }

    @Test
    public void testDeleteInvalidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.delete(2L);
        assertTrue(catOptional.isEmpty());
    }

    @Test
    public void testUpdateNullCatThrowsError(){
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.update(null));
        assertEquals("entity must not be null", exception.getMessage());
    }

    @Test
    public void testUpdateValidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.update(newCat);
        assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(newCat.getId()) &&
                catOptional.get().getName().equals(newCat.getName()) &&
                catOptional.get().getOwner().equals(newCat.getOwner()) &&
                catOptional.get().getHumanYears().equals(newCat.getHumanYears()) &&
                StreamSupport.stream(repository.findAll().spliterator(), false).count() == 1
        );
    }

    @Test
    public void testUpdateInvalidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.update(new Cat(2L, "a","a",2));
        assertTrue(catOptional.isEmpty());
    }
}
