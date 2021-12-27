package rabbitmq_rcp;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
public class RpcCallerConsumer extends DefaultConsumer {
    public RpcCallerConsumer(Channel channel) {
        super(channel);
    }

    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws java.io.IOException {

        String messageIdentifier = properties.getCorrelationId();
        String action = actions.get(messageIdentifier);
        actions.remove(messageIdentifier);

        String response = new String(body);
        OnReply(action, response);
    }
}
