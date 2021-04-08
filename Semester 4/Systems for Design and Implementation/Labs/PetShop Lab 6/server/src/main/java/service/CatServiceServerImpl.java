package service;

import common.domain.Cat;
import common.domain.CatFood;
import common.domain.Pair;
import common.domain.Purchase;
import common.exceptions.PetShopException;
import common.service.ICatService;
import repository.IRepository;

import java.util.Set;
import java.util.stream.StreamSupport;

import static java.lang.Math.max;

public class CatServiceServerImpl implements ICatService {
    final IRepository<Long, Cat> catsRepository;
    final IRepository<Pair<Long, Long>, Purchase> purchaseRepository;
    final IRepository<Pair<Long, Long>, CatFood> catFoodRepository;

    public CatServiceServerImpl(IRepository<Long, Cat> catsRepository, IRepository<Pair<Long, Long>, Purchase> purchaseRepository, IRepository<Pair<Long, Long>, CatFood> catFoodRepository) {
        this.catsRepository = catsRepository;
        this.purchaseRepository = purchaseRepository;
        this.catFoodRepository = catFoodRepository;
    }

    @Override
    public void addCat(String name, String breed, Integer catYears) {
        long id = 0;
        for (Cat cat :this.catsRepository.findAll())
            id = max(id, cat.getId()+1);
        Cat catToBeAdded = new Cat(id, name, breed, catYears);
        catsRepository.save(catToBeAdded);
    }

    @Override
    public Set<Cat> getCatsFromRepository() {
        return (Set<Cat>) catsRepository.findAll();
    }


    @Override
    public void deleteCat(Long id) {
        StreamSupport.stream(catFoodRepository.findAll().spliterator(), false)
                .filter(catFood -> catFood.getCatId().equals(id))
                .findAny()
                .ifPresent((catFood) -> {
                    throw new PetShopException("Cat is currently fed");
                });
        StreamSupport.stream(purchaseRepository.findAll().spliterator(), false)
                .filter(purchase -> purchase.getCatId().equals(id))
                .findAny()
                .ifPresent(purchase -> {
                    throw new PetShopException("The cat is purchased, can't delete");
                });
        catsRepository.delete(id).orElseThrow(() -> new PetShopException("Cat does not exist"));
    }

    @Override
    public void updateCat(Long id, String name, String breed, Integer catYears) {
        catsRepository.update(new Cat(id, name, breed, catYears))
                .orElseThrow(() -> new PetShopException("Cat does not exist"));
    }
}
