package domain.validators;

import domain.Cat;
import domain.exceptions.ValidatorException;

public class CatValidator implements Validator<Cat>{
    @Override
    public void validate(Cat cat) throws ValidatorException {
        if(cat.getName().isEmpty()){
            throw new ValidatorException("Cat name must not be empty");
        }

        if(cat.getOwner().isEmpty()){
            throw new ValidatorException("Cat owner name must not be empty");
        }

        if(cat.getCatYears() < 0){
            throw new ValidatorException("Cat years must be a positive integer");
        }
    }
}
