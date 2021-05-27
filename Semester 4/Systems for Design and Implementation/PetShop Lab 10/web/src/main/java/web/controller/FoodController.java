package web.controller;

import core.domain.Food;
import core.service.IFoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.FoodConverter;
import web.dto.FoodDTO;
import web.dto.FoodsDto;

import java.util.List;

@RestController
public class FoodController {
    public static final Logger logger = LoggerFactory.getLogger(FoodController.class);

    @Autowired
    private IFoodService foodService;

    @Autowired
    private FoodConverter foodConverter;

    @RequestMapping(value = "/food")
    FoodsDto getFoodFromRepository() {
        logger.trace("getAllFood - method entered");
        List<Food> food = foodService.getFoodFromRepository();
        FoodsDto foodsDto = new FoodsDto(foodConverter.convertModelsToDTOs(food));
        logger.trace("getAllFood: " + food);
        return foodsDto;
    }

    @RequestMapping(value = "/food", method = RequestMethod.POST)
    void addFood(@RequestBody FoodDTO foodDTO) {
        logger.trace("addFood - method entered - foodDto: " + foodDTO);
        var food = foodConverter.convertDtoToModel(foodDTO);
        foodService.addFood(
                food.getName(),
                food.getProducer(),
                food.getExpirationDate()
        );
        logger.trace("addFood - food added");
    }

    @RequestMapping(value = "/food/{id}", method = RequestMethod.PUT)
    void updateFood(@PathVariable Long id, @RequestBody FoodDTO foodDTO) {
        logger.trace("updateFood - method entered - foodDTO: " + foodDTO);
        var food = foodConverter.convertDtoToModel(foodDTO);
        foodService.updateFood(
                id,
                food.getName(),
                food.getProducer(),
                food.getExpirationDate()
        );
        logger.trace("updateFood - food updated");
    }

    @RequestMapping(value = "/food/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCat(@PathVariable Long id) {
        foodService.deleteFood(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

