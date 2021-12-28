package rabbitmq_rpc;

import com.rabbitmq.client.*;
import io.vertx.core.json.JsonObject;
import vertx.ultiliities.Operation.Operation;
import vertx.ultiliities.validation.Validation;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "aaaaaa";


    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
//            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();
                String message = new String(delivery.getBody(), "UTF-8");

                Operation ope = new Operation();
                Validation valid = new Validation();
                try {
                    JsonObject param = new JsonObject(message);
                    if (valid.isNumeric(String.valueOf(param.getValue("a"))) &&
                            valid.isNumeric(String.valueOf(param.getValue("b")))) {

                        double a = param.getDouble("a", 0.0);
                        double b = param.getDouble("b", 1.0);
                        String op = param.getString("op", "+");

                        if (valid.validateOpe(b, op) == 1) {
                            double result = ope.operation(a, b, op);
                            System.out.println("Got message: " + param.encodePrettily());
                            message="Result: "+String.valueOf(result);
                        } else if (valid.validateOpe(b, op) == 0) {
                            message="You can't put 0 under the denominator";
                            System.out.println("You can't put 0 under the denominator");
                            return;
                        } else {
                            message="Please enter the right operation (+, -, x, :)";
                            System.out.println("Please enter the right operation (+, -, x, :)");
                            return;
                        }
                    } else {
                        System.out.println("Please enter the right number");
                        return;
                    }
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, message.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));

            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}