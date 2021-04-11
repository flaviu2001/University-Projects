package core.domain.validators;

import core.domain.Cat;
import core.domain.Pair;
import core.exceptions.ValidatorException;

import java.util.stream.Stream;

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
