package common.domain.validators;

import common.domain.Pair;
import common.domain.Purchase;
import common.exceptions.ValidatorException;

import java.util.Date;
import java.util.stream.Stream;

public class PurchaseValidator implements Validator<Purchase> {

    @Override
    public void validate(Purchase entity) throws ValidatorException {
        Stream.of(new Pair<>(entity.getPrice() < 0, "Purchase price must not be lower than zero"),
                new Pair<>(entity.getReview() < 1, "Review must have at least one star"),
                new Pair<>(entity.getReview() > 5, "Review must have at most five stars"),
                new Pair<>(entity.getDateAcquired().after(new Date()), "Purchase must have a date in the past")
                ).filter(Pair::getLeft)
                .forEach(invalidSituation -> {throw new ValidatorException(invalidSituation.getRight());});
    }
}
