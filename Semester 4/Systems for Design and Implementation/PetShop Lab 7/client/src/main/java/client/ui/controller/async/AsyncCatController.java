package client.ui.controller.async;

import common.domain.Cat;
import common.exceptions.PetShopException;
import common.service.ICatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class AsyncCatController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncCatController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private ICatService catService;

    public CompletableFuture<Iterable<Cat>> getCatsFromRepository(){
        return CompletableFuture.supplyAsync(catService::getCatsFromRepository, executorService);
    }

    public CompletableFuture<String> addCat(String name, String breed, Integer catYears){
        logger.trace("addCat - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(()->{
            try{
                catService.addCat(name, breed, catYears);
                return "Cat added";
            }
            catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteCat(Long id){
        logger.trace("deleteCat - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(()->{
            try{
                catService.deleteCat(id);
                return "Cat deleted";
            }
            catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears){
        logger.trace("updateCat - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(()->{
            try{
                catService.updateCat(id, name, breed, catYears);
                return "Cat updated";
            }
            catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

}
