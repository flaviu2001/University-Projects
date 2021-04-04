package server;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Starting server...");
        new AnnotationConfigApplicationContext("server.config");
    }
}
