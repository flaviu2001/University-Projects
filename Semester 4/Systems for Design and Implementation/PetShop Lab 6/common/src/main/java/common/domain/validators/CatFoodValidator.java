package common.domain.validators;

import common.domain.CatFood;
import common.exceptions.ValidatorException;

public class CatFoodValidator implements Validator<CatFood> {
    @Override
    public void validate(CatFood entity) throws ValidatorException {
        //Nothing to validate
    }
}
