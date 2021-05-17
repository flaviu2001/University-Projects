package core.repository;

import core.domain.Toy;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToyRepository extends Repository<Toy, Long>{

    @Query("select distinct t from Toy t")
    @EntityGraph(value = "toysWithCats", type = EntityGraph.EntityGraphType.LOAD)
    List<Toy> findToysWithCats();
}
