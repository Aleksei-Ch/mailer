package io.mailer.mailer.rpc;

import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mailer.mailer.model.EmailRequest;
import io.mailer.mailer.smtp_client.EMailSender;

@Component
public class EmailSenderRpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSenderRpcServer.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ExecutorService execs = Executors.newCachedThreadPool();

    private final ConnectionFactory factory;

    @Autowired
    private EMailSender sender;

    public EmailSenderRpcServer(
            @Value("${amqp.server}") String server,
            @Value("${amqp.user}") String user,
            @Value("${amqp.password}") String password,
            @Value("${amqp.email_queue}") String queue
        ) throws Exception {

        factory = new ConnectionFactory();
        factory.setHost(server);
        factory.setUsername(user);
        factory.setPassword(password);

        new Thread(() -> run(queue)).start();
    }

    public void run(String queue) {

        try (var connection = factory.newConnection();
            var channel = connection.createChannel()) {

            // Declare the queue if it is not declared yet
            channel.queueDeclare(queue, true, false, false, null);

            channel.basicConsume(queue, false, new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                        byte[] body) {

                    final long deliveryTag = envelope.getDeliveryTag();
                    final byte[] deliveryBody = body;

                    Runnable processor = () -> {
                        var replyProperties = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();

                        var response = new TreeMap<String, Object>();

                        try {
                            var req = mapper.readValue(deliveryBody, EmailRequest.class);
                            var res = sender.send(req);
                            response.put("result", res);
                        } catch (Exception e) {
                            LOG.error("Cannot process the message. ", e);
                        } finally {
                            try {
                                channel.basicPublish("", properties.getReplyTo(), replyProperties, mapper.writeValueAsBytes(response));
                                channel.basicAck(deliveryTag, false);
                            } catch (IOException e) {
                                LOG.error("An error occurred. ", e);
                            }
                        }

                    };

                    execs.submit(processor);
                }

            });

            while(true) {
                // Do not finish the Thread
                Thread.sleep(60 * 1000);
            }

        } catch (Exception e) {
            LOG.error("An error occurred while running EmailSenderRpcServer: ", e);
        }

    }

}