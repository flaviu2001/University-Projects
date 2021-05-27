package core.service;

import core.domain.Cat;
import core.domain.Purchase;
import core.exceptions.PetShopException;
import core.exceptions.ValidatorException;

import java.util.List;

public interface CatService {
    /**
     * Saves the cat with the given attributes to the core.repository of cats.
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    void addCat(String name, String breed, Integer catYears);
    /**
     * @return all cats from the core.repository.
     */
    List<Cat> getCatsFromRepository();

    /**
     * @param breed The breed by which to filter
     * @return the list of cats of that breed
     */
    List<Cat> getCatsByBreed(String breed);

    List<Purchase> findPurchasesOfCatByCatId(long catId);

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
