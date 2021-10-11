package client;

import client.service.ClientPetShipService;
import client.tcp.TcpClient;
import client.ui.UI;
import common.PetShopService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        TcpClient tcpClient = new TcpClient();
        PetShopService petShopService = new ClientPetShipService(executorService, tcpClient);
        UI clientConsole = new UI(petShopService);
        clientConsole.runProgram();
        executorService.shutdown();
    }
}
