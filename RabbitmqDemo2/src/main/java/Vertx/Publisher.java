package Vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;


public class Publisher extends AbstractVerticle {
    public void start() {
        RabbitMQOptions config = new RabbitMQOptions();
    // Config specify individual parameters
        config.setUser("guest");
        config.setPassword("guest");
        config.setHost("localhost");
        config.setPort(5672);

        RabbitMQClient client = RabbitMQClient.create(vertx, config);

        JsonObject msg = new JsonObject().put("a", 10).put("b", 5).put("op", ":");
        // Connect
        client.start(asyncResult -> {
            if (asyncResult.succeeded()) {
                System.out.println("RabbitMQ successfully connected!");
                client.basicPublish("", "aaaaaa", msg.toBuffer(), pubResult -> {
                    if (pubResult.succeeded()) {
                        System.out.println("Message published !");
                    } else {
                        pubResult.cause().printStackTrace();
                    }
                });
            } else {
                System.out.println("Fail to connect to RabbitMQ " + asyncResult.cause().getMessage());
            }
        });
    }
}
