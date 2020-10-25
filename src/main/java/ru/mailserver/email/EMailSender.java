package ru.mailserver.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.mailserver.model.EmailRequest;

import javax.mail.util.ByteArrayDataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class EMailSender {

    private final static Logger LOG = LoggerFactory.getLogger(EMailSender.class);

    private final EmailQueue emailQueue;

    private final JavaMailSender emailSender;

    public EMailSender(EmailQueue emailQueue, JavaMailSender emailSender) {
        this.emailQueue = emailQueue;
        this.emailSender = emailSender;

        var executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (this.emailQueue.hasNext()) {
                send(this.emailQueue.next());
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void send(EmailRequest req) {

        try {
            var message = emailSender.createMimeMessage();

            var helper = new MimeMessageHelper(message, true);

            helper.setFrom(req.getFrom());
            helper.setTo(req.getTo());
            if (req.getCc() != null) {
                helper.setCc(req.getCc());
            }
            if (req.getBcc() != null) {
                helper.setBcc(req.getBcc());
            }
            helper.setSubject(req.getTitle());
            helper.setText(req.getBody());

            if (req.getAttachments() != null) {
                req.getAttachments().forEach((key, value) -> {
                    if (key != null && value != null) {
                        try {
                            helper.addAttachment(key,
                                    new ByteArrayDataSource(value, "application/octet-stream"));
                        } catch (Exception e) {
                            LOG.error("Cannot add attachment", e);
                        }
                    }
                });
            } 

            emailSender.send(message);

        } catch (Exception e) {
            LOG.error("An error occurred while sending message: ", e);
        }

    }

}