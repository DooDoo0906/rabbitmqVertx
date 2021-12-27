package rabbitmq_rcp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class ActualConsumer extends DefaultConsumer {
    public ActualConsumer(Channel channel) {
        super(channel);
    }

    public void handleDelivery(
            String consumerTag,
            Envelope envelope,
            AMQP.BasicProperties properties,
            byte[] body) throws IOException {
        String message = new String(body);
        System.out.println("Received: " + message);
    }
}