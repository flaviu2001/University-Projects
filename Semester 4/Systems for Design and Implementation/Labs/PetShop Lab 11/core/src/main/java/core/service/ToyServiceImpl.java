package core.service;

import core.domain.Cat;
import core.domain.Toy;
import core.exceptions.PetShopException;
import core.repository.ToyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;

@Service
public class ToyServiceImpl implements ToyService {
    @Autowired
    private ToyRepository toyRepository;

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
        return toyRepository.findAll();
    }

    @Override
    public List<Toy> getToysOfCat(Long catId) {
        return toyRepository.findAll().stream().filter(toy -> toy.getCat().getId().equals(catId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteToy(Long id) {
        toyRepository.findById(id)
                .ifPresentOrElse((toy) -> {
                            toy.getCat().getToys().remove(toy);
                            toyRepository.deleteById(toy.getId());
                        },
                        () -> {
                            throw new PetShopException("Toy does not exist");
                        }
                );
    }

    @Override
    @Transactional
    public void updateToy(Long id, String name, int price, Cat cat) {
        toyRepository.findById(id).ifPresentOrElse((toy -> {
            toy.setName(name);
            toy.setPrice(price);
            toy.setCat(cat);
        }), ()->{
            throw new PetShopException("Nonexistent toy");
        });
    }
}
