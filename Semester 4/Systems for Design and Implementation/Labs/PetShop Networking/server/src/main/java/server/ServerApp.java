package server;


import common.Convertor;
import common.Message;
import common.PetShopService;
import common.domain.Cat;
import common.domain.Food;
import server.service.ServerPetShopService;
import server.tcp.TcpServer;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServerApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        TcpServer tcpServer = new TcpServer(executorService, PetShopService.PORT);
        PetShopService petShopService = new ServerPetShopService(executorService);

        tcpServer.addHandler(PetShopService.GET_CATS, request ->{
            try{
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getCatsFromRepository().get().spliterator(), false)
                        .map(Convertor::convertCat)
                        .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_CAT, request ->{
            try{
                String[] splitCat = request.getBody().split(",");
                String status = petShopService.addCat(splitCat[0], splitCat[1], Integer.parseInt(splitCat[2])).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.DELETE_CAT, request -> {
            try {
                String status = petShopService.deleteCat(Long.parseLong(request.getBody())).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_CAT, request -> {
            try {
                Cat cat = Convertor.extractCat(request.getBody());
                String status = petShopService.updateCat(cat.getId(), cat.getName(), cat.getBreed(), cat.getCatYears()).get();
                return new Message(Message.ERROR, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.GET_FOOD, request -> {
            try {
                return new Message(Message.OK,
                        StreamSupport.stream(petShopService.getFoodFromRepository().get().spliterator(), false)
                                .map(Convertor::convertFood)
                                .collect(Collectors.joining(";")));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.ADD_FOOD, request -> {
            try{
                String[] splitFood = request.getBody().split(",");
                Date date = new Date(Long.parseLong(splitFood[2]));
                String status = petShopService.addFood(splitFood[0], splitFood[1], date).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.DELETE_FOOD, request -> {
            try {
                String status = petShopService.deleteFood(Long.parseLong(request.getBody())).get();
                return new Message(Message.OK, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(PetShopService.UPDATE_FOOD, request -> {
            try {
                Food food = Convertor.extractFood(request.getBody());
                String status = petShopService.updateFood(food.getId(), food.getName(), food.getProducer(), food.getExpirationDate()).get();
                return new Message(Message.ERROR, status);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.startServer();
    }
}
