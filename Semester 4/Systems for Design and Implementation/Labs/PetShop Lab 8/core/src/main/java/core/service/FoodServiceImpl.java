package core.service;

import core.domain.Food;
import core.exceptions.PetShopException;
import core.repository.IFoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static java.lang.Math.max;

@Service
public class FoodServiceImpl implements IFoodService {
    @Autowired
    private IFoodRepository foodRepository;

    public static final Logger logger = LoggerFactory.getLogger(FoodServiceImpl.class);

    @Override
    public void addFood(String name, String producer, Date expirationDate) {
        logger.trace("add food - method entered - name: " + name + ", producer: " + producer + ", expiration date: " + expirationDate.toString());
        long id = 0;
        for (Food food : this.foodRepository.findAll())
            id = max(id, food.getId() + 1);
        Food foodToBeAdded = new Food(id, name, producer, expirationDate);
        foodRepository.save(foodToBeAdded);
        logger.trace("add food - method finished");
    }

    @Override
    public List<Food> getFoodFromRepository() {
        logger.trace("getFoodFromRepository - method entered");
        List<Food> food = foodRepository.findAll();
        logger.trace("getFoodFromRepository: " + food.toString());
        return food;
    }


    @Override
    public void deleteFood(Long id) {
        logger.trace("deleteFood - method entered - id: " + id);
        foodRepository.findById(id)
                .ifPresentOrElse((food) -> foodRepository.deleteById(food.getId()),
                        () -> {
                            throw new PetShopException("Food does not exist");
                        }
                );
        logger.trace("deleteFood - method finished");

    }

    @Override
    @Transactional
    public void updateFood(Long id, String name, String producer, Date expirationDate) {
        logger.trace("updateFood - method entered - id: " + id + ", name: " + name + ", producer: " + producer + ", expiration date: " + expirationDate.toString());
        foodRepository.findById(id)
                .ifPresentOrElse((food) -> {
                            food.setName(name);
                            food.setProducer(producer);
                            food.setExpirationDate(expirationDate);
                            System.out.println(food);
                        },
                        () -> {
                            throw new PetShopException("Food does not exist");
                        }
                );
        logger.trace("update food - method finished");

    }
}
