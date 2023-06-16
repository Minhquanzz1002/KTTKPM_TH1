package vn.edu.iuh;

import org.apache.log4j.BasicConfigurator;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class QueueReceiver {
    public static void main(String[] args) throws NamingException, JMSException {
        BasicConfigurator.configure();
        Properties settings = new Properties();
        settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        Context ctx = new InitialContext(settings);

        Object obj = ctx.lookup("ConnectionFactory");
        ConnectionFactory factory = (ConnectionFactory) obj;
        Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");

        Connection connection =factory.createConnection("admin","admin");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer receiver = session.createConsumer(destination);
        System.out.println("Quân was listened on queue...");

        receiver.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage tm = (TextMessage) message;
                        String txt = tm.getText();
                        System.out.println("Receiver: " + txt);
                        message.acknowledge();
                    } else if (message instanceof ObjectMessage) {
                        ObjectMessage om = (ObjectMessage) message;
                        System.out.println(om);
                    }
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
