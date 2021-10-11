package client.ui.controller.async;

import core.domain.Cat;
import core.exceptions.PetShopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.CatDTO;
import web.dto.CatsDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncCatController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncCatController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<Cat>> getCatsFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/cats";
                CatsDTO cats = restTemplate.getForObject(url, CatsDTO.class);
                if (cats == null)
                    throw new PetShopException("Could not retrieve cats from server");
                return cats.getCats().stream().map(catDTO -> new Cat(catDTO.getId(), catDTO.getName(), catDTO.getBreed(), catDTO.getCatYears())).collect(Collectors.toSet());
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

    public CompletableFuture<String> addCat(String name, String breed, Integer catYears) {
        logger.trace("addCat - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/cats";
                restTemplate.postForObject(url,
                        new CatDTO(name, breed, catYears),
                        CatDTO.class);
                return "Cat added";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteCat(Long id) {
        logger.trace("deleteCat - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/cats";
                restTemplate.delete(url + "/{id}", id);
                return "Cat successfully deleted";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

    public CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears) {
        logger.trace("updateCat - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/cats";
                CatDTO catToUpdate = new CatDTO(name, breed, catYears);
                catToUpdate.setId(id);
                restTemplate.put(url + "/{id}", catToUpdate, catToUpdate.getId());
                return "Cat successfully updated";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

}
