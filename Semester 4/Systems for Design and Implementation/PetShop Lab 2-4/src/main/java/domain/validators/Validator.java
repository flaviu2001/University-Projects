package domain.validators;
import domain.exceptions.ValidatorException;
public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
