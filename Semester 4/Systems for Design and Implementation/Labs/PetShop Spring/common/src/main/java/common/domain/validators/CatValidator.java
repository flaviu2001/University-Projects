package common.domain.validators;

import common.domain.Cat;
import common.exceptions.ValidatorException;
import common.domain.Pair;
import java.util.stream.*;
public class CatValidator implements Validator<Cat>{
    @Override
    public void validate(Cat cat) throws ValidatorException {
        Stream.of(new Pair<>(cat.getName().isEmpty(), "Cat name must not be empty"),
                new Pair<>(cat.getBreed().isEmpty(), "Cat breed must not be empty"),
                new Pair<>(cat.getCatYears() < 0, "Cat must be 0 years old or older"))
                .filter(Pair::getLeft)
                .forEach((b) -> {
                    throw new ValidatorException(b.getRight());
                });
    }
}
