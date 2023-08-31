package qu4lizz.factoryordersapp.service;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import qu4lizz.factoryordersapp.Application;
import qu4lizz.factoryordersapp.utils.ConfigUtil;
import qu4lizz.factoryordersapp.utils.Logger;

public class MailService {
    private static final String username;
    private static final String password;
    private static final Properties properties;

    static {
        try {
            properties = new Properties();
            String mailConfigPath = ConfigUtil.getProperties().getProperty("mail_config_path");
            properties.load(new FileInputStream(Application.class.getResource(mailConfigPath).getFile()));
            String mailCredentialPath = ConfigUtil.getProperties().getProperty("env_path");
            properties.load(new FileInputStream(Application.class.getResource(mailCredentialPath).getFile()));

            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMail(String to, String title, String body) throws IOException {
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username,"Factory"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(title);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
