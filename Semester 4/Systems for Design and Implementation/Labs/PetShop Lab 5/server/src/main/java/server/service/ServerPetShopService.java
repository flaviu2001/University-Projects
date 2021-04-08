package server.service;

import common.PetShopService;
import common.domain.Cat;
import common.domain.Food;
import common.domain.validators.*;
import common.exceptions.PetShopException;
import repository.databaseRepository.*;
import service.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class ServerPetShopService implements PetShopService {
    private final ExecutorService executorService;
    private Service service;

    public ServerPetShopService(ExecutorService executorService) {
        this.executorService = executorService;
        initialiseDatabaseApplication();
    }

    private HashMap<String, String> readSettingsFile() {
        HashMap<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        String configFile = "data/programData/settings.properties";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(configFile);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return propertiesMap;
        }
        Stream.ofNullable(fileInputStream).findAny().ifPresentOrElse((el) ->  {
            try {
                properties.load(fileInputStream);
                propertiesMap.put("database", properties.getProperty("database"));
                propertiesMap.put("user", properties.getProperty("user"));
                propertiesMap.put("password", properties.getProperty("password"));
            } catch (IOException ioException) {
                System.out.println("IOException: " + ioException.getMessage());
            }
        }, () -> System.out.println("Invalid config file"));

        return propertiesMap;
    }

    void initialiseDatabaseApplication() {
        HashMap<String, String> configurations = readSettingsFile();
        service = new Service(
                new CatDatabaseRepository(new CatValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new FoodDatabaseRepository(new FoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new CatFoodDatabaseRepository(new CatFoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new CustomerDatabaseRepository(new CustomerValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new PurchaseDatabaseRepository(new PurchaseValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password"))
        );
    }

    @Override
    public CompletableFuture<Iterable<Cat>> getCatsFromRepository() {
        return CompletableFuture.supplyAsync(() -> service.getCatsFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addCat(String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.addCat(name, breed, catYears);
                return "Cat added successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCat(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.deleteCat(id);
                return "Cat deleted successfully";
            } catch (PetShopException e) {
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                service.updateCat(id, name, breed, catYears);
                return "Cat updated successfully";
            } catch (PetShopException e) {
                return e.getMessage();
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Food>> getFoodFromRepository() {
        return CompletableFuture.supplyAsync(() -> service.getFoodFromRepository(), executorService);
    }

    @Override
    public CompletableFuture<String> addFood(String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.addFood(name, producer, expirationDate);
                return "Food added successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteFood(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.deleteFood(id);
                return "Food deleted successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateFood(Long id, String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                service.updateFood(id, name, producer, expirationDate);
                return "Food updated successfully";
            }
            catch (PetShopException e){
                return e.getMessage();
            }

        }, executorService);
    }
}
