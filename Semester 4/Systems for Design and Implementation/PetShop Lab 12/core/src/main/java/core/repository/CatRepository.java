package core.repository;

import core.domain.Cat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatRepository extends Repository<Cat, Long>, CatExtendedRepository {
    List<Cat> findCatsByBreedLike(String breed);

    @Query("select distinct c from Cat c where c.id = :catId")
    @EntityGraph(value = "catsWithToys", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Cat> findCatByIdWithToys(@Param("catId") long catId);
}
