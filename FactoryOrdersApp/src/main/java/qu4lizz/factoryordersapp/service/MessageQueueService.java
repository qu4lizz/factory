package qu4lizz.factoryordersapp.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;
import qu4lizz.factoryordersapp.Application;
import qu4lizz.factoryordersapp.model.OrderWrapper;
import qu4lizz.factoryordersapp.utils.ConfigUtil;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class MessageQueueService {
    private static Connection createConnection() throws IOException, TimeoutException {
        Properties properties = ConfigUtil.getProperties();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getProperty("mq_host"));
        factory.setUsername(properties.getProperty("mq_username"));
        factory.setPassword(properties.getProperty("mq_password"));
        return factory.newConnection();
    }

    public OrderWrapper takeOrder() throws IOException, TimeoutException, JAXBException {
        OrderWrapper order = null;
        Properties properties = ConfigUtil.getProperties();

        try (Connection connection = createConnection()) {
            Channel channel = connection.createChannel();
            String messageQueueName = properties.getProperty("mq_name");
            channel.queueDeclare(messageQueueName, true, false, false, null);
            GetResponse response = channel.basicGet(messageQueueName, false);

            if (response != null) {
                byte[] body = response.getBody();
                String xml = new String(body, StandardCharsets.UTF_8);

                if (schemaValidation(xml)) {
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    order = convertFromXML(xml);
                }
            }

            channel.close();
        }
        return order;
    }

    private OrderWrapper convertFromXML(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(OrderWrapper.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return (OrderWrapper) jaxbUnmarshaller.unmarshal(reader);
    }

    private boolean schemaValidation(String xml) {
        try {
            String schemaPath = Application.class.getResource(ConfigUtil.getProperties().getProperty("xml_schema_path")).getPath();
            Source schemaSource = new StreamSource(schemaPath);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaSource);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (SAXException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
