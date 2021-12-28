package vertx;

import io.vertx.core.Vertx;

public class DeployConsumer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Consumer());
    }
}
