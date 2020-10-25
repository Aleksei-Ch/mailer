package ru.mailserver.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mailserver.model.EmailRequest;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class EmailQueue {

    private static final Logger LOG = LoggerFactory.getLogger(EmailQueue.class);

    private static final Deque<EmailRequest> Q = new ConcurrentLinkedDeque<>();

    public boolean addMessage(EmailRequest r) {
        var result = true;
        try {
            Q.addLast(r);
            LOG.info("Added new message to the queue. Count: " + Q.size());
        } catch (Exception e) {
            result = false;
            LOG.error("Cannot add new message to the queue. ", e);
        }
        return result;
    }

    public boolean hasNext() {
        return Q.size() > 0;
    }

    public EmailRequest next() {
        EmailRequest result = null;
        if (hasNext()) {
            result = Q.pop();
            LOG.info("Extracted message from the queue. Last count: " + Q.size());
        }
        return result;
    }

}
