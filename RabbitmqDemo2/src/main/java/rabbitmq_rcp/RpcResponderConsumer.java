package rabbitmq_rcp;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class RpcResponderConsumer extends DefaultConsumer{
    public RpcResponderConsumer(Channel channel) {
        super(channel);
    }

    public void handleDelivery(
            String consumerTag,
            Envelope envelope,
            AMQP.BasicProperties properties,
            byte[] body) throws IOException {
        String message = new String(body);

        AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
        getChannel().basicPublish("", properties.getReplyTo(),replyProperties, message.getBytes());
        getChannel().basicAck(envelope.getDeliveryTag(), false);
        System.out.println("Received: " + message);
    }
}
