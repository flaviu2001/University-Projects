package web.converter;

import core.domain.Toy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.dto.ToyDTO;

@Component
public class ToyConverter extends BaseConverter<Long, Toy, ToyDTO> {
    @Autowired
    private CatConverter catConverter;

    @Override
    public Toy convertDtoToModel(ToyDTO dto) {
        var model = new Toy();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setCat(catConverter.convertDtoToModel(dto.getCat()));
        return model;
    }

    @Override
    public ToyDTO convertModelToDto(Toy toy) {
        var dto = new ToyDTO(catConverter.convertModelToDto(toy.getCat()), toy.getName(), toy.getPrice());
        dto.setId(toy.getId());
        return dto;
    }
}
