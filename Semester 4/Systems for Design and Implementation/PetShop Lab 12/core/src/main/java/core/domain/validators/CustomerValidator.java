package core.domain.validators;

import core.domain.Customer;
import core.domain.Pair;
import core.exceptions.ValidatorException;

import java.util.stream.Stream;

public class CustomerValidator implements Validator<Customer>{
    @Override
    public void validate(Customer customer) throws ValidatorException {
        Stream.of(new Pair<>(customer.getName().isEmpty(), "Customer name must not be empty"),
                new Pair<>(customer.getPhoneNumber().length() != 10, "Customer phone number must have 10 digits"))
                .filter(Pair::getLeft)
                .forEach((item) -> {
                    throw new ValidatorException(item.getRight());
                });
    }
}
