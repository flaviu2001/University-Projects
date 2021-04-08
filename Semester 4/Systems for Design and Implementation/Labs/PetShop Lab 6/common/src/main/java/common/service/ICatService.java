package common.service;

import common.domain.Cat;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;

import java.util.Set;

public interface ICatService {
    /**
     * Saves the cat with the given attributes to the repository of cats.
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    void addCat(String name, String breed, Integer catYears);
    /**
     * @return all cats from the repository.
     */
    Set<Cat> getCatsFromRepository();

    /**
     * Deletes a cat based on it's id
     *
     * @param id - id of the cat to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat does not exist
     *                                  if the cat is currently fed
     */
    void deleteCat(Long id);

    /**
     * Updates the cat with the given attributes.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    void updateCat(Long id, String name, String breed, Integer catYears);

}
