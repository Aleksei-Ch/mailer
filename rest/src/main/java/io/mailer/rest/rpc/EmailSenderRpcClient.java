package io.mailer.rest.rpc;

import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
    private final ConnectionFactory factory;
    
    private static final String QUEUE = "MAILER_EMAIL";

    public EmailSenderRpcClient(
            @Value("${amqp.server}") String server,
            @Value("${amqp.user}") String user,
            @Value("${amqp.password}") String password
            ) throws Exception {
                
        factory = new ConnectionFactory();
        factory.setHost(server);
        factory.setUsername(user);
        factory.setPassword(password);

        try (var conn = factory.newConnection();
            var channel = conn.createChannel()) {

            // Declare the queue if it is not declared yet
            channel.queueDeclare(QUEUE, true, false, false, null);

        } catch (Exception e) {
            LOG.error("Can not initialize EmailSenderRpcClient: ", e);
            throw e;
        }
    }

    public Object exchange(EmailRequest req) {

        Object result = null;

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
                mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8)
            );

            final BlockingQueue<String> resp = new ArrayBlockingQueue<>(1);

            var ctag = channel.basicConsume(replyTo, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                    resp.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
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