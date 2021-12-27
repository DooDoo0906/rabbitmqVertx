package DemoRabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory= new ConnectionFactory();

        try(Connection connection= factory.newConnection()){
            Channel channel= connection.createChannel();
            channel.queueDeclare("rabbitmq-demo",false,false,false,null);

            String message ="is this matrix?";
            channel.basicPublish("","rabbitmq_demo",false,null,message.getBytes());
            System.out.println("!!!Message sent");

        }

    }
}
