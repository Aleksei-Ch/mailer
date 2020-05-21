package io.mailer.rest.rpc;

import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;

import io.mailer.rest.model.EmailRequest;

@Component
public class EmailSenderRpcClient {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(EmailSenderRpcClient.class);
    
    private static final String QUEUE = "MAILER_EMAIL";

    @Value("${ampq.server}")
    private String server;

    @Value("${ampq.user}")
    private String user;

    @Value("${ampq.password}")
    private String password;

    public Object exchange(EmailRequest req) {

        Object result = null;

        var factory = new ConnectionFactory();
        factory.setHost(server);
        factory.setUsername(user);
        factory.setPassword(password);

        try (var conn = factory.newConnection();
            var channel = conn.createChannel()) {
                
                final var correlationId = UUID.randomUUID().toString();
                final var replyTo = channel.queueDeclare().getQueue();
                
                var props = new AMQP.BasicProperties().builder()
                    .correlationId(correlationId)
                    .replyTo(replyTo)
                    .build();

                channel.basicPublish(
                    "", 
                    QUEUE, 
                    props, 
                    mapper.writeValueAsString(req).getBytes("UTF-8")
                );

                final BlockingQueue<String> resp = new ArrayBlockingQueue<>(1);

                var ctag = channel.basicConsume(replyTo, true, (consumerTag, delivery) -> {
                    if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                        resp.offer(new String(delivery.getBody(), "UTF-8"));
                    }
                }, consumerTag -> {});

                result = mapper.readValue(resp.take(), Map.class);
                channel.basicCancel(ctag);

        } catch (Exception e) {
            LOG.error("An error occurred while sending email: ", e);
        }

        return result;
    }

}