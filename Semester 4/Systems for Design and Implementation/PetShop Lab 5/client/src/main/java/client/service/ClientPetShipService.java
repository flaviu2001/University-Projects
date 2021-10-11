package client.service;


import client.tcp.TcpClient;
import common.Convertor;
import common.Message;
import common.PetShopService;
import common.domain.Cat;
import common.domain.Food;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static common.Convertor.convertCat;

public class ClientPetShipService implements PetShopService {
    private final ExecutorService executorService;
    private final TcpClient tcpClient;

    public ClientPetShipService(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public CompletableFuture<Iterable<Cat>> getCatsFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_CATS, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Cat> cats = new LinkedList<>();

            Arrays.stream(result.split(";"))
                    .map(Convertor::extractCat)
                    .forEach(cats::add);
            return cats;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addCat(String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.ADD_CAT, name+","+breed+","+catYears);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteCat(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.DELETE_CAT, "" + id);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears) {
        return CompletableFuture.supplyAsync(() -> {
            Cat cat = new Cat(id, name, breed, catYears);
            Message request = new Message(PetShopService.UPDATE_CAT, convertCat(cat));
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<Iterable<Food>> getFoodFromRepository() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.GET_FOOD, "");

            Message response = tcpClient.sendAndReceive(request);

            String result = response.getBody();
            List<Food> foods = new LinkedList<>();

            Arrays.stream(result.split(";"))
                    .map(Convertor::extractFood)
                    .forEach(foods::add);
            return foods;
        }, executorService);
    }

    @Override
    public CompletableFuture<String> addFood(String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.ADD_FOOD, name + "," + producer + "," + expirationDate.getTime());
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> deleteFood(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.DELETE_FOOD, "" + id);
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }

    @Override
    public CompletableFuture<String> updateFood(Long id, String name, String producer, Date expirationDate) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(PetShopService.UPDATE_FOOD, id + "," + name + "," + producer + "," + expirationDate.getTime());
            return tcpClient.sendAndReceive(request).getBody();
        }, executorService);
    }
}