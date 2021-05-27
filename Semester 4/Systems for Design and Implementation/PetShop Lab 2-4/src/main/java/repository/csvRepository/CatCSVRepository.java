package repository.csvRepository;

import domain.Cat;
import domain.validators.Validator;

import java.util.Arrays;
import java.util.List;

public class CatCSVRepository extends CSVRepository<Long, Cat>{

    public CatCSVRepository(Validator<Cat> validator, String filePath) {
        super(validator, filePath);
    }

    /**
     * @param dataTransferObject is a line from the csv file
     * @return Cat object corresponding to the string
     */
    @Override
    protected Cat extractEntity(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long id = Long.parseLong(tokens.get(0));
        String name = tokens.get(1);
        String breed = tokens.get(2);
        Integer catYears = Integer.parseInt(tokens.get(3));
        return new Cat(id, name, breed, catYears);
    }

    /**
     * @param entity Cat to be mapped to a string
     * @return string in csv format containing the Cat's attributes
     */
    @Override
    protected String convertEntity(Cat entity) {
        return  entity.getId() + "," +
                entity.getName() + "," +
                entity.getBreed() + "," +
                entity.getCatYears();
    }
}
