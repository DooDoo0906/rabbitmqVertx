package vertx;

import vertx.ultiliities.Operation.Operation;
import vertx.ultiliities.validation.Validation;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQConsumer;
import io.vertx.rabbitmq.RabbitMQOptions;

public class Consumer extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        RabbitMQOptions config = new RabbitMQOptions();
        config.setUser("guest");
        config.setPassword("guest");
        config.setHost("localhost");
        config.setPort(5672);
        config.setAutomaticRecoveryOnInitialConnection(true);

        RabbitMQClient server = RabbitMQClient.create(vertx, config);

        // Connect
        Operation ope = new Operation();
        Validation valid = new Validation();
        server.start().onSuccess(v -> {
            server.basicConsumer("aaaaaa", rabbitMQConsumerAsyncResult -> {
                if (rabbitMQConsumerAsyncResult.succeeded()) {
                    System.out.println("RabbitMQ consumer created !");

                    //Receiving message
                    RabbitMQConsumer mqConsumer = rabbitMQConsumerAsyncResult.result();
                    mqConsumer.handler(message -> {
                        JsonObject param = new JsonObject(message.body());
                        if (valid.isNumeric(String.valueOf(param.getValue("a"))) &&
                                valid.isNumeric(String.valueOf(param.getValue("b")))) {
                            double a = param.getDouble("a", 0.0);
                            double b = param.getDouble("b", 1.0);
                            String op = param.getString("op", "+");
                            if (valid.validateOpe(b, op) == 1) {
                                double result = ope.operation(a, b, op);
                                System.out.println("Result: " + result);
                            } else if (valid.validateOpe(b, op) == 0) {
                                System.out.println("You can't put 0 under the denominator");
                            } else {
                                System.out.println("Please enter the right operation (+, -, x, :)");
                            }
                        } else {
                            System.out.println("Please enter the right number");
                        }
                    });
                } else {
                    rabbitMQConsumerAsyncResult.cause().printStackTrace();
                }
            });

        });
    }
}