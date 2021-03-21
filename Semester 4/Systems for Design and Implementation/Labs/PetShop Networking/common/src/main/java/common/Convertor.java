package common;

import common.domain.Cat;
import common.domain.Food;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Convertor {

    /**
     * @param dataTransferObject String in csv format to be converted to Cat
     * @return Cat
     */
    public static Cat extractCat(String dataTransferObject) {
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
    public static String convertCat(Cat entity) {
        return  entity.getId() + "," +
                entity.getName() + "," +
                entity.getBreed() + "," +
                entity.getCatYears();
    }

    /**
     * @param dataTransferObject String in csv format to be converted to Food
     * @return Food
     */
    public static Food extractFood(String dataTransferObject)  {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long id = Long.parseLong(tokens.get(0));
        String name = tokens.get(1);
        String producer = tokens.get(2);
        long expirationDate = Long.parseLong(tokens.get(3));
        Date date = new Date();
        date.setTime(expirationDate);
        return new Food(id, name, producer, date);
    }

    /**
     * @param food Food to be mapped to a string
     * @return string in csv format containing the Food's attributes
     */
    public static String convertFood(Food food) {
        return food.getId() + "," +
                food.getName() + "," +
                food.getProducer() + "," +
                food.getExpirationDate().getTime();
    }
}
