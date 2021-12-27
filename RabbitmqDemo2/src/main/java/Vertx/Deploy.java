package Vertx;

import io.vertx.core.Vertx;

public class Deploy {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(new Publisher());
        vertx.deployVerticle(new Consumer());
    }
}
