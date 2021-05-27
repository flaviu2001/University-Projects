package web.converter;

import core.domain.Food;
import org.springframework.stereotype.Component;
import web.dto.FoodDTO;

@Component
public class FoodConverter extends BaseConverter<Long, Food, FoodDTO> {

    @Override
    public Food convertDtoToModel(FoodDTO dto) {
        var model = new Food();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setProducer(dto.getProducer());
        model.setExpirationDate(dto.getExpirationDate());
        return model;
    }

    @Override
    public FoodDTO convertModelToDto(Food food) {
        var foodDto = new FoodDTO(food.getName(), food.getProducer(), food.getExpirationDate());
        foodDto.setId(food.getId());
        return foodDto;
    }
}
