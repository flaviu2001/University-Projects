package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ToyController {
    private final String toyUrl = "http://toys:8080/api";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/toys")
    Object getToys() {
        return restTemplate.getForObject(toyUrl + "/toys", Object.class);
    }

    @RequestMapping(value = "/toys/{id}")
    Object getToysByCatId(@PathVariable Long id) {
        return restTemplate.getForObject(toyUrl + "/toys/" + id, Object.class);
    }

    @RequestMapping(value = "/toys", method = RequestMethod.POST)
    void addToy(@RequestBody Object toy) {
        restTemplate.postForObject(toyUrl + "/toys", toy, Object.class);
    }

    @RequestMapping(value = "/toys/{id}", method = RequestMethod.PUT)
    void updateToy(@PathVariable Long id, @RequestBody Object toy) {
        restTemplate.put(toyUrl + "/toys/" + id, toy);
    }

    @RequestMapping(value = "/toys/{id}", method = RequestMethod.DELETE)
    void deleteToy(@PathVariable Long id) {
        restTemplate.delete(toyUrl + "/toys/" + id);
    }
}
