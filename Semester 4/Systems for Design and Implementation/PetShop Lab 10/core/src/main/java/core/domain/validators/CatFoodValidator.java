package core.domain.validators;

import core.domain.CatFood;
import core.exceptions.ValidatorException;

public class CatFoodValidator implements Validator<CatFood> {
    @Override
    public void validate(CatFood entity) throws ValidatorException {
        //Nothing to validate
    }
}
