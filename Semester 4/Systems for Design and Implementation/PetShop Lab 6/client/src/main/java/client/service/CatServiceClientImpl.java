package client.service;

import common.domain.Cat;
import common.service.ICatService;
import common.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class CatServiceClientImpl implements ICatService {
    @Autowired
    private ICatService catService;

    /**
     * Saves the cat with the given attributes to the repository of cats.
     *
     * @param name name of cat
     * @param breed breed of cat
     * @param catYears age of cat
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */
    @Override
    public void addCat(String name, String breed, Integer catYears) {
        catService.addCat(name, breed, catYears);
    }

    /**
     * @return all cats from the repository.
     */
    @Override
    public Set<Cat> getCatsFromRepository() {
        return catService.getCatsFromRepository();
    }

    /**
     * Deletes a cat based on it's id
     *
     * @param id - id of the cat to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat does not exist
     *                                  if the cat is currently fed
     */
    @Override
    public void deleteCat(Long id) {
        catService.deleteCat(id);
    }

    /**
     * Updates the cat with the given attributes.
     *
     * @param id       must not be null
     * @param name name of cat
     * @param breed breed of cat
     * @param catYears age of cat
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */
    @Override
    public void updateCat(Long id, String name, String breed, Integer catYears) {
        catService.updateCat(id, name, breed, catYears);
    }
}
