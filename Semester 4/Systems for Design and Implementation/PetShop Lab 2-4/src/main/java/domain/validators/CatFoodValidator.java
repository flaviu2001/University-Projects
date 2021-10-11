package domain.validators;

import domain.CatFood;
import domain.exceptions.ValidatorException;

public class CatFoodValidator implements Validator<CatFood> {
    @Override
    public void validate(CatFood entity) throws ValidatorException {
        //Nothing to validate
    }
}
