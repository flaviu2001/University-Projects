package web.controller;

import core.domain.CatFood;
import core.service.ICatFoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.CatFoodConverter;
import web.dto.CatFoodPrimaryKeyDTO;
import web.dto.CatFoodsDTO;

import java.util.List;

@RestController
public class CatFoodController {
    public static final Logger logger = LoggerFactory.getLogger(CatFoodController.class);

    @Autowired
    private ICatFoodService catFoodService;

    @Autowired
    private CatFoodConverter catFoodConverter;

    @RequestMapping(value = "/catFoods")
    CatFoodsDTO getCatFoodFromRepository(){
        logger.trace("getCatFoodFromRepository - method entered");
        List<CatFood> catFoods = catFoodService.getCatFoodFromRepository();
        CatFoodsDTO catFoodsDTO = new CatFoodsDTO(catFoodConverter.convertModelsToDTOs(catFoods));
        logger.trace("getAllCatFoods: " + catFoods);
        return catFoodsDTO;
    }

    @RequestMapping(value = "/catFoods", method = RequestMethod.POST)
    void addCatFood(@RequestBody CatFoodPrimaryKeyDTO catFoodPrimaryKeyDTO){
        logger.trace("addCatFood - method entered - catFoodDTO: " + catFoodPrimaryKeyDTO);
        catFoodService.addCatFood(catFoodPrimaryKeyDTO.getCatId(), catFoodPrimaryKeyDTO.getFoodId());
        logger.trace("addCatFood - catFood added");
    }

    @RequestMapping(value = "/catFoods/{newId}", method = RequestMethod.PUT)
    void updateCatFood(@PathVariable Long newId, @RequestBody CatFoodPrimaryKeyDTO catFoodDTO){
        logger.trace("updateCatFood - method entered - catFoodDTO: " + catFoodDTO);
        catFoodService.updateCatFood(catFoodDTO.getCatId(), catFoodDTO.getFoodId(), newId);
        logger.trace("updateCatFood - catFood updated");
    }

    @RequestMapping(value = "/catFoods/{catId}&{foodId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCatFood(@PathVariable Long catId, @PathVariable Long foodId) {
        catFoodService.deleteCatFood(catId, foodId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/catFoods/{foodId}", method = RequestMethod.GET)
    CatFoodsDTO filterCatsThatEatCertainFood(@PathVariable Long foodId){
        logger.trace("filterCatsThatEatCertainFood - method entered");
        List<CatFood> catFoods = catFoodService.filterCatsThatEatCertainFood(foodId);
        CatFoodsDTO catFoodsDTO = new CatFoodsDTO(catFoodConverter.convertModelsToDTOs(catFoods));
        logger.trace("filterCatsThatEatCertainFood: " + catFoods);
        return catFoodsDTO;
    }
}
