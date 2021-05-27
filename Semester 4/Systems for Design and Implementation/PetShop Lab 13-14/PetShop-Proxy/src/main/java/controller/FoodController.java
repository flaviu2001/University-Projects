package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@RestController
public class FoodController {
    private final String foodUrl = "http://foods:3001";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/foods")
    Object getFoods() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        return restTemplate.getForObject(foodUrl + "/foods", Object.class, entity);
    }

    @RequestMapping(value = "/foods", method = RequestMethod.POST)
    void addFood(@RequestBody Object food) {
        restTemplate.postForObject(foodUrl + "/foods", food, Object.class);
    }

    @RequestMapping(value = "/foods", method = RequestMethod.PUT)
    void updateFood(@RequestBody Object food) {
        restTemplate.put(foodUrl + "/foods", food);
    }

    @RequestMapping(value = "/foods/{id}", method = RequestMethod.DELETE)
    void deleteFood(@PathVariable Long id) {
        restTemplate.delete(foodUrl + "/foods/" + id);
    }
}
