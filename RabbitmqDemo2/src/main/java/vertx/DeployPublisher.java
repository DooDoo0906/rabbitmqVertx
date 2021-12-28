package vertx;

import io.vertx.core.Vertx;

public class DeployPublisher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Publisher());
    }
}
