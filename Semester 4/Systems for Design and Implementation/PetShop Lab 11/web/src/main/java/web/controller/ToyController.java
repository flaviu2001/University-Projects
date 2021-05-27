package web.controller;

import core.domain.Toy;
import core.service.ToyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.ToyConverter;
import web.dto.ToyDTO;
import web.dto.ToysDTO;

import java.util.List;

@RestController
public class ToyController {
    @Autowired
    private ToyService toyService;

    @Autowired
    private ToyConverter toyConverter;

    @RequestMapping(value = "/toys")
    ToysDTO getToysFromRepository() {
        List<Toy> toys = toyService.getToys();
        return new ToysDTO(toyConverter.convertModelsToDTOs(toys));
    }

    @RequestMapping(value = "/toys/{id}")
    ToysDTO getToysFromRepositoryByCatId(@PathVariable Long id) {
        List<Toy> toys = toyService.getToysOfCat(id);
        return new ToysDTO(toyConverter.convertModelsToDTOs(toys));
    }

    @RequestMapping(value = "/toys", method = RequestMethod.POST)
    void addToy(@RequestBody ToyDTO toyDTO) {
        var toy = toyConverter.convertDtoToModel(toyDTO);
        toyService.addToy(
                toy.getName(),
                toy.getPrice(),
                toy.getCat()
        );
    }

    @RequestMapping(value = "/toys/{id}", method = RequestMethod.PUT)
    void updateToy(@PathVariable Long id, @RequestBody ToyDTO toyDTO) {
        var toy = toyConverter.convertDtoToModel(toyDTO);
        toyService.updateToy(
                id,
                toy.getName(),
                toy.getPrice(),
                toy.getCat()
        );
    }

    @RequestMapping(value = "/toys/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteToy(@PathVariable Long id) {
        toyService.deleteToy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
