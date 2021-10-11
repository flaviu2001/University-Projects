package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class CatController {
    private final String catUrl = "http://cats:8080/api";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/cats")
    Object getCats() {
        return restTemplate.getForObject(catUrl + "/cats", Object.class);
    }

    @RequestMapping(value = "/cats", method = RequestMethod.POST)
    void addCat(@RequestBody Object cat) {
        restTemplate.postForObject(catUrl + "/cats", cat, Object.class);
    }

    @RequestMapping(value = "/cats/{breed}")
    Object getCatsByBreed(@PathVariable String breed) {
        return restTemplate.getForObject(catUrl + "/cats/" + breed, Object.class);
    }

    @RequestMapping(value = "/cats/{id}", method = RequestMethod.PUT)
    void updateCat(@PathVariable Long id, @RequestBody Object catDTO) {
        restTemplate.put(catUrl + "/cats/" + id, catDTO);
    }

    @RequestMapping(value = "/cats/{id}", method = RequestMethod.DELETE)
    void deleteCat(@PathVariable Long id) {
        restTemplate.delete(catUrl + "/cats/" + id);
    }
}
