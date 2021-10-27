package web.converter;

import core.domain.BaseEntity;
import web.dto.BaseDTO;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseConverter<ID extends Serializable, Model extends BaseEntity<ID>, DTO extends BaseDTO<ID>> implements Converter<ID, Model, DTO> {
    public Set<ID> convertModelsToIDs(Collection<Model> models) {
        return models.stream().map(BaseEntity::getId).collect(Collectors.toSet());
    }

    public Set<ID> convertDTOsToIDs(Collection<DTO> dtos) {
        return dtos.stream().map(BaseDTO::getId).collect(Collectors.toSet());
    }

    public Set<DTO> convertModelsToDTOs(Collection<Model> models) {
        return models.stream().map(this::convertModelToDto).collect(Collectors.toSet());
    }

}
