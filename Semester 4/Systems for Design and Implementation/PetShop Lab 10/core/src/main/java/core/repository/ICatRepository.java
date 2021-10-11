package core.repository;

import core.domain.Cat;

import java.util.List;

public interface ICatRepository extends IRepository<Cat, Long>{
    List<Cat> findCatsByBreedLike(String breed);
}
