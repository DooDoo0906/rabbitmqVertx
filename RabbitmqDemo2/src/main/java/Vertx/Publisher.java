package Vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;


public class Publisher extends AbstractVerticle {
    public void start() {
        RabbitMQOptions config = new RabbitMQOptions();
    // Each parameter is optional
        config.setUser("guest");
        config.setPassword("guest");
        config.setHost("localhost");
        config.setPort(5672);

        RabbitMQClient client = RabbitMQClient.create(vertx, config);

    // Connect
        JsonObject js = new JsonObject().put("a", 10).put("b", 0).put("op", ":");



        client.start(asyncResult -> {
            if (asyncResult.succeeded()) {
                System.out.println("RabbitMQ successfully connected!");
                Buffer message = Buffer.buffer(js.toString());
                client.basicPublish("", "aaaaaa", message, pubResult -> {
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


//        client.queueDeclare("aaaaaa", false, false, false, queueResult -> {
//            if (queueResult.succeeded()) {
//                System.out.println("Declared!");
//            } else {
//                System.err.println("Failed ");
//                queueResult.cause().printStackTrace();
//            }
//        });

    }
}
