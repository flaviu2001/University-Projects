package client.ui.controller.async;

import common.domain.Cat;
import common.exceptions.PetShopException;
import common.service.ICatService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AsyncCatController {
    ExecutorService executorService;
    private final ICatService catService;

    public AsyncCatController(ExecutorService executorService, ICatService catService) {
        this.executorService = executorService;
        this.catService = catService;
    }

    public CompletableFuture<Iterable<Cat>> getCatsFromRepository(){
        return CompletableFuture.supplyAsync(catService::getCatsFromRepository, executorService);
    }

    public CompletableFuture<String> addCat(String name, String breed, Integer catYears){
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
