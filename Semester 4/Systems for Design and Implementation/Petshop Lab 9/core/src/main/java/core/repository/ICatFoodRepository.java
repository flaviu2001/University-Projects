package core.repository;

import core.domain.CatFood;
import core.domain.CatFoodPrimaryKey;
import core.domain.Food;

import java.util.List;

public interface ICatFoodRepository extends IRepository<CatFood, CatFoodPrimaryKey> {
    List<CatFood> findAllByFood(Food food);
}
