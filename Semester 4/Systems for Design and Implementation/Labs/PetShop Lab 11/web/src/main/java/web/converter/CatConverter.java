package web.converter;

import core.domain.Cat;
import org.springframework.stereotype.Component;
import web.dto.CatDTO;

@Component
public class CatConverter extends BaseConverter<Long, Cat, CatDTO> {
    @Override
    public Cat convertDtoToModel(CatDTO dto) {
        var model = new Cat();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setBreed(dto.getBreed());
        model.setCatYears(dto.getCatYears());
        return model;
    }

    @Override
    public CatDTO convertModelToDto(Cat cat) {
        var catDto = new CatDTO(cat.getName(), cat.getBreed(), cat.getCatYears());
        catDto.setId(cat.getId());
        return catDto;
    }
}
