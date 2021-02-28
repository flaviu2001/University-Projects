package domain.validators;

import domain.exceptions.ValidatorException;
import domain.Food;

public class FoodValidator implements Validator<Food>{
    @Override
    public void validate(Food food) throws ValidatorException {
        if(food.getName().isEmpty()){
            throw new ValidatorException("Food name must not be empty");
        }

        if(food.getProducer().isEmpty()){
            throw new ValidatorException("Food producer name must not be empty");
        }
    }
}
