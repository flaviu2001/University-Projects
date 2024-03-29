package core.service;

import core.domain.Toy;

import java.util.List;

public interface ToyService {
    void addToy(String name, int price, long catId);
    List<Toy> getToys();
    List<Toy> getToysOfCat(Long catId);
    void deleteToy(Long id);
    void updateToy(Long id, String name, int price);
}
