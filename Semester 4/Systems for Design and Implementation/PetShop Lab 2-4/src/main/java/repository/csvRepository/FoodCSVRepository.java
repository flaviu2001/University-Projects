package repository.csvRepository;

import domain.Food;
import domain.exceptions.PetShopException;
import domain.validators.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FoodCSVRepository extends CSVRepository<Long, Food> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public FoodCSVRepository(Validator<Food> validator, String filePath) {
        super(validator, filePath);
    }

    /**
     * @param dataTransferObject is a line from the csv file
     * @return Food object corresponding to the string
     */
    @Override
    protected Food extractEntity(String dataTransferObject) {
        try{
            List<String> tokens = Arrays.asList(dataTransferObject.split(","));
            Long id = Long.parseLong(tokens.get(0));
            String name = tokens.get(1);
            String producer = tokens.get(2);
            Date expirationDate = dateFormat.parse(tokens.get(3));
            return new Food(id, name, producer, expirationDate);
        } catch (ParseException e) {
            throw new PetShopException(e.getMessage());
        }

    }

    /**
     * @param entity Food to be mapped to a string
     * @return string in csv format containing the Food's attributes
     */
    @Override
    protected String convertEntity(Food entity) {
        return  entity.getId() + "," +
                entity.getName() + "," +
                entity.getProducer() + "," +
                dateFormat.format(entity.getExpirationDate());
    }
}
