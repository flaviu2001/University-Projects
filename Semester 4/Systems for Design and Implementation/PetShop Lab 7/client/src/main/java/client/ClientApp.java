package client;

import client.ui.UI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("client.config");
        UI ui = context.getBean(UI.class);
        ui.runProgram();
        System.out.println("Bye");
    }
}
