package common.domain.validators;
import common.exceptions.ValidatorException;
public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
