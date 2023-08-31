package qu4lizz.clientapp.service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import qu4lizz.clientapp.model.OrderWrapper;
import qu4lizz.clientapp.utils.ConfigUtil;
import qu4lizz.clientapp.utils.Logger;

public class MessageQueueService {
    private final String host;
    private final String username;
    private final String password;
    private final String queueName;
    public MessageQueueService() {
        var properties = ConfigUtil.getProperties();
        host = properties.getProperty("mq_host");
        username = properties.getProperty("mq_username");
        password = properties.getProperty("mq_password");
        queueName = properties.getProperty("mq_queue");
        System.out.println("Message Queue running on " + host);
    }

    public void sendOrder(OrderWrapper order) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);

        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);

            String orderString = convertToXML(order);

            channel.basicPublish("", queueName, null, orderString.getBytes(StandardCharsets.UTF_8));
        } catch (JAXBException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private String convertToXML(OrderWrapper order) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(OrderWrapper.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(order, sw);
        return sw.toString();
    }
}
