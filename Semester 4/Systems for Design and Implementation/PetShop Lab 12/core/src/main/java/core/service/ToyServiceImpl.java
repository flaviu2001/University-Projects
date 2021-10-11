package core.service;

import core.domain.Cat;
import core.domain.Toy;
import core.exceptions.PetShopException;
import core.repository.CatRepository;
import core.repository.ToyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;

@Service
public class ToyServiceImpl implements ToyService {
    @Autowired
    private ToyRepository toyRepository;

    @Autowired
    private CatRepository catRepository;

    @Override
    public void addToy(String name, int price, Cat cat) {
        long id = 0;
        for (Toy toy : this.toyRepository.findAll())
            id = max(id, toy.getId() + 1);
        Toy toy = new Toy(id, name, price);
        toy.setCat(cat);
        toyRepository.save(toy);
    }

    @Override
    public List<Toy> getToys() {
        return toyRepository.findToysWithCats();
    }

    @Override
    public List<Toy> getToysOfCat(Long catId) {
        Optional<Cat> cat = catRepository.findCatByIdWithToys(catId);
        if (cat.isPresent()) {
            return new ArrayList<>(cat.get().getToys());
        }
        throw new PetShopException("Cat not found in get toys of cat");
    }

    @Override
    @Transactional
    public void deleteToy(Long id) {
        toyRepository.findById(id)
                .ifPresent((toy) -> {
                            toy.getCat().getToys().remove(toy);
                            toyRepository.deleteById(toy.getId());
                        }
                );
    }

    @Override
    @Transactional
    public void updateToy(Long id, String name, int price, Cat cat) {
        toyRepository.findById(id).ifPresent((toy -> {
            toy.setName(name);
            toy.setPrice(price);
            toy.setCat(cat);
        }));
    }
}
