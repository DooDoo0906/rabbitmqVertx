package rabbitmq_rcp;

import com.rabbitmq.client.*;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            String myQueue = "aaaaaa";
            channel.queueDeclare(myQueue, true, false, false, null);

//            AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
            JsonObject msg = new JsonObject().put("a", 10).put("b", 5).put("op", ":");

            channel.basicPublish("", myQueue, null, msg.encodePrettily().getBytes());
            System.out.println("Message published: ");
            System.out.println(msg.encodePrettily());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
