package com.ubb.en.ArcaneProgramming.service;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService implements Runnable {
    //created an email for the app to send verification emails:
    //email:         arcaneprogramming.nimda@gmail.com
    //password:      admin69420
    //app-password:  klxopseflryukkqn
    private static final String email = "arcaneprogramming.nimda@gmail.com";
    private static final String username = "arcaneprogramming.nimda@gmail.com";
    private static final String password = "klxopseflryukkqn";

    private final String title;
    private final String body;
    private final String destination;


    public MailService(String title, String body, String destination) {
        this.title = title;
        this.body = body;
        this.destination = destination;
    }


    @Override
    public void run() {
        System.out.println("Messaging Started\n");
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        System.out.println("Trying to get session\n");
        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });

        System.out.println("Session created successfully\n");
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destination)
            );

            message.setSubject(title);
            message.setText(body);
            System.out.println("Try to send");
            Transport.send(message);
            System.out.println("Message sent!\n");

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

}
