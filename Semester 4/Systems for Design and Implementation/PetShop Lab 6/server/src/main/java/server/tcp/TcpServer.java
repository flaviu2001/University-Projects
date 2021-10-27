package server.tcp;


import common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class TcpServer {
    private final ExecutorService executorService;
    private final int port;
    private final Map<String, UnaryOperator<Message>> methodHandlers;

    public TcpServer(ExecutorService executorService, int port) {
        this.executorService = executorService;
        this.port = port;
        this.methodHandlers = new HashMap<>();

    }

    public void addHandler(String methodName, UnaryOperator<Message> handler) {
        methodHandlers.put(methodName, handler);
    }

    public void startServer() {
        try (var serverSocket = new ServerSocket(this.port)) {
            System.out.println("server started; waiting for clients...");
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected");
                executorService.submit(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (var is = socket.getInputStream();
                 var os = socket.getOutputStream()) {

                //read the request (of type Message) from client
                Message request = new Message();
                request.readFrom(is);
                System.out.println("received request: " + request);

                // compute response (of type Message)
                Message response = methodHandlers.get(request.getHeader()).apply(request);
                System.out.println("computed response: " + response);

                //send response (of type Message) to client
                response.writeTo(os);
                System.out.println("response sent to client");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
