/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.ex.webmarket.controller;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
/**
 *
 * @author jessviozzi
 */
public class EmailSender {
    public static void sendEmail(String to, String subject, String body) {
        
        //gmail
        String from = "webmarket.univaq@gmail.com";
        String password = "edaz dstf frvf xfac";
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

       /* 
        //Outlook
        String from = "webmarket.univaq@outlook.com";
        String password = "geagiuliasamanta1";
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");*/

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email inviata con successo a " + to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
 public static void main(String[] args) {
        sendEmail("destinatario@example.com", "Oggetto dell'email", "Corpo dell'email");
    }
    
}
