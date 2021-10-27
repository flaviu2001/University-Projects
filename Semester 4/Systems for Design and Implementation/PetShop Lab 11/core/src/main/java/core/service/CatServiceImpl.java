package core.service;

import core.domain.Cat;
import core.exceptions.PetShopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import core.repository.CatRepository;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Math.max;

import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    public static final Logger logger = LoggerFactory.getLogger(CatServiceImpl.class);

    @Autowired
    private CatRepository catsRepository;

    @Override
    public void addCat(String name, String breed, Integer catYears) {
        logger.trace("add cat - method entered - name: " + name + ", breed: " + breed + ", catYears: " + catYears);
        long id = 0;
        for (Cat cat : this.catsRepository.findAll())
            id = max(id, cat.getId() + 1);
        Cat catToBeAdded = new Cat(id, name, breed, catYears);
        catsRepository.save(catToBeAdded);
        logger.trace("add cat - method finished");
    }

    @Override
    public List<Cat> getCatsFromRepository() {
        logger.trace("getCatsFromRepository - method entered");
        List<Cat> cats = catsRepository.findAll();
        logger.trace("getCatsFromRepository: " + cats);
        return cats;
    }

    @Override
    public List<Cat> getCatsByBreed(String breed) {
        logger.trace("getCatsByBreed - method entered");
        var toReturn = catsRepository.findCatsByBreedLike(breed);
        logger.trace("getCatsByBreed - method exited");
        return toReturn;
    }

    @Override
    public void deleteCat(Long id) {
        logger.trace("deleteCat - method entered - id: " + id);

        catsRepository.findById(id)
                .ifPresentOrElse((cat) -> catsRepository.deleteById(cat.getId()),
                        () -> {
                            throw new PetShopException("Cat does not exist");
                        }
                );

//        catsRepository.deleteById(id).orElseThrow(() -> new PetShopException("Cat does not exist"));
        logger.trace("deleteCat - method finished");
    }

    @Override
    @Transactional
    public void updateCat(Long id, String name, String breed, Integer catYears) {
        logger.trace("updateCat - method entered - id: " + id + ", name: " + name + ", breed: " + breed +
                ", catYears: " + catYears);

        catsRepository.findById(id)
                .ifPresentOrElse((cat) -> {
                            cat.setName(name);
                            cat.setBreed(breed);
                            cat.setCatYears(catYears);
                        },
                        () -> {
                            throw new PetShopException("Cat does not exist");
                        }
                );
        logger.trace("updateCat - method finished");
    }
}
