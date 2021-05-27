package web.converter;

import core.domain.Toy;
import org.springframework.stereotype.Component;
import web.dto.ToyDTO;

@Component
public class ToyConverter extends BaseConverter<Long, Toy, ToyDTO> {

    @Override
    public Toy convertDtoToModel(ToyDTO dto) {
        Toy model = new Toy();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setCatId(dto.getCatId());
        return model;
    }

    @Override
    public ToyDTO convertModelToDto(Toy toy) {
        ToyDTO dto = new ToyDTO(toy.getCatId(), toy.getName(), toy.getPrice());
        dto.setId(toy.getId());
        return dto;
    }
}
