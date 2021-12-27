package rabbitmq_rcp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            String myQueue="aaaaaa";
            channel.queueDeclare(myQueue, true, false, false, null);
            ActualConsumer consumer = new ActualConsumer(channel);
            String consumerTag = channel.basicConsume(myQueue, true, consumer);
            //stop the consumer
            channel.basicCancel(consumerTag);
        }
    }
}
