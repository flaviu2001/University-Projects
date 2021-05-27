package core.domain.validators;

import core.domain.Food;
import core.domain.Pair;
import core.exceptions.ValidatorException;

import java.util.stream.Stream;

public class FoodValidator implements Validator<Food>{
    @Override
    public void validate(Food food) throws ValidatorException {
        Stream.of(new Pair<>(food.getName().isEmpty(), "Food name must not be empty"),
                new Pair<>(food.getProducer().isEmpty(), "Food producer must not be empty"))
                .filter(Pair::getLeft)
                .forEach((b) -> {
                    throw new ValidatorException(b.getRight());
                });
    }
}
