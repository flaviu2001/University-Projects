package repository;

import domain.Cat;
import domain.validators.CatValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class InMemoryRepositoryTest {
    private static final Cat cat = new Cat(1L, "name", "owner", 2);
    private static final Cat newCat = new Cat(1L, "newName", "newOwner", 3);

    private InMemoryRepository<Long, Cat> repository;

    @BeforeEach
    public void setUp(){
        repository = new InMemoryRepository<>(new CatValidator());
    }

    @AfterEach
    public void tearDown(){
        repository = null;
    }

    @Test
    public void testSaveValidCat(){
        Optional<Cat> catOptional = repository.save(cat);
        Assertions.assertTrue(catOptional.isEmpty());
    }

    @Test
    public void testSaveExistentCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.save(cat);
        Assertions.assertTrue(catOptional.isPresent());
    }

    @Test
    public void testSaveNullCatThrowsError(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    public void testFindOneNullIdThrowsException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findOne(null));
    }

    @Test
    public void testFindOneExistentId(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.findOne(1L);
        Assertions.assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(cat.getId()) &&
                catOptional.get().getName().equals(cat.getName()) &&
                catOptional.get().getBreed().equals(cat.getBreed()) &&
                catOptional.get().getHumanYears().equals(cat.getHumanYears())

        );
    }

    @Test
    public void testDeleteNullCatThrowsError(){
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.delete(null));
        Assertions.assertEquals("id must not be null", exception.getMessage());
    }

    @Test
    public void testDeleteValidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.delete(1L);
        Assertions.assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(cat.getId()) &&
                catOptional.get().getName().equals(cat.getName()) &&
                catOptional.get().getBreed().equals(cat.getBreed()) &&
                catOptional.get().getHumanYears().equals(cat.getHumanYears()) &&
                StreamSupport.stream(repository.findAll().spliterator(), false).count() == 0
        );
    }

    @Test
    public void testDeleteInvalidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.delete(2L);
        Assertions.assertTrue(catOptional.isEmpty());
    }

    @Test
    public void testUpdateNullCatThrowsError(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.update(null));
    }

    @Test
    public void testUpdateValidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.update(newCat);
        Assertions.assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(newCat.getId()) &&
                catOptional.get().getName().equals(newCat.getName()) &&
                catOptional.get().getBreed().equals(newCat.getBreed()) &&
                catOptional.get().getHumanYears().equals(newCat.getHumanYears()) &&
                StreamSupport.stream(repository.findAll().spliterator(), false).count() == 1
        );
    }

    @Test
    public void testUpdateInvalidCat(){
        repository.save(cat);
        Optional<Cat> catOptional = repository.update(new Cat(2L, "a","a",2));
        Assertions.assertTrue(catOptional.isEmpty());
    }


}
