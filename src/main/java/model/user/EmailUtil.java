/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.user;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    public static void sendConfirmationEmail(String toEmail, String userName, String eventName, int quantity) {
        String subject = "Ticket Purchase Confirmation";
        String body = "Dear " + userName + ",\n\n"
                    + "You have successfully purchased " + quantity + " ticket(s) for the event: " + eventName + ".\n\n"
                    + "Thank you for your purchase!\n\nBest regards,\nEvent Management Team";

        // SMTP Server Details
        String host = "smtp.gmail.com";
        String port = "587";
        String fromEmail = "your_email@gmail.com"; // Use your email
        String password = "your_email_password";   // Your email password

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Confirmation email sent.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}