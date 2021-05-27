package client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import client.ui.UI;

public class ClientApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("client.config");
        UI ui = context.getBean(UI.class);
        ui.runProgram();
        System.out.println("Bye");
    }
}
