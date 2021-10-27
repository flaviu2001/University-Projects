package core.repository;

import core.domain.Cat;

import java.util.List;

public interface CatRepository extends Repository<Cat, Long> {
    List<Cat> findCatsByBreedLike(String breed);
}
