package web.converter;

import core.domain.CatFood;
import core.domain.CatFoodPrimaryKey;
import org.springframework.stereotype.Component;
import web.dto.CatFoodDTO;

@Component
public class CatFoodConverter extends BaseConverter<CatFoodPrimaryKey, CatFood, CatFoodDTO> {
    @Override
    public CatFood convertDtoToModel(CatFoodDTO dto) {
        var model = new CatFood();
        model.setId(dto.getId());
        model.setCat(dto.getCat());
        model.setFood(dto.getFood());
        return model;
    }

    @Override
    public CatFoodDTO convertModelToDto(CatFood catFood) {
        var catFoodDto = new CatFoodDTO();
        catFoodDto.setId(catFood.getId());
        catFoodDto.setCat(catFood.getCat());
        catFoodDto.setFood(catFood.getFood());
        return catFoodDto;
    }
}
