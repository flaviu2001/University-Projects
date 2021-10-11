package repository.csvRepository;

import domain.CatFood;
import domain.Pair;
import domain.validators.Validator;

import java.util.Arrays;
import java.util.List;

public class CatFoodCSVRepository extends CSVRepository<Pair<Long, Long>, CatFood> {
    public CatFoodCSVRepository(Validator<CatFood> validator, String filePath) {
        super(validator, filePath);
    }

    /**
     * @param dataTransferObject is a line from the csv file
     * @return CatFood object corresponding to the string
     */
    @Override
    protected CatFood extractEntity(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long catId = Long.parseLong(tokens.get(0));
        Long foodId = Long.parseLong(tokens.get(1));
        return new CatFood(catId, foodId);
    }

    /**
     * @param entity CatFood to be mapped to a string
     * @return string in csv format containing the CatFood's attributes
     */
    @Override
    protected String convertEntity(CatFood entity) {
        return entity.getCatId() + "," + entity.getFoodId();
    }
}
