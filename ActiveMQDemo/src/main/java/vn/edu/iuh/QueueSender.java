package vn.edu.iuh;


import jakarta.xml.bind.JAXBException;
import org.apache.log4j.BasicConfigurator;
import vn.edu.iuh.data.Person;
import vn.edu.iuh.helper.XMLConvert;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Date;
import java.util.Properties;

public class QueueSender {
    public static void main(String[] args) throws NamingException, JMSException, JAXBException {
        BasicConfigurator.configure();
        Properties settings = new Properties();
        settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        Context ctx = new InitialContext(settings);

        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
        Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
        Connection connection = factory.createConnection("admin", "admin");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(destination);
        Message message = session.createTextMessage("Hello message from ActiveMQ");
        producer.send(message);

        Person person = new Person(2, "Nguyễn Minh Quân", new Date());
        String xml = new XMLConvert<Person>(person).object2XML(person);

        message = session.createTextMessage(xml);
        producer.send(message);
        session.close();
        connection.close();
        System.out.println("Finished");
    }
}
