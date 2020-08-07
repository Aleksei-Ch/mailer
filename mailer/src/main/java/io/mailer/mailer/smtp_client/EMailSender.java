package io.mailer.mailer.smtp_client;

import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import io.mailer.mailer.model.EmailRequest;

/**
 * An example SMTP client
 */
@Component
public class EMailSender {

    private final static Logger LOG = LoggerFactory.getLogger(EMailSender.class);

    public enum Status {
        SENT, ERROR
    }

    @Autowired
    private JavaMailSender emailSender;

    public Status send(@Valid EmailRequest req) {

        Status result = null;

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
                req.getAttachments().entrySet().forEach(entry -> {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        try {
                            helper.addAttachment(entry.getKey(),
                                    new ByteArrayDataSource(entry.getValue(), "application/octet-stream"));
                        } catch (MessagingException e) {
                            LOG.error("Cannot add attachment", e);
                        }
                    }
                });
            } 
        
            emailSender.send(message);

            result = Status.SENT;
        } catch (MessagingException e) {
            LOG.error("An error occurred while sending message: ", e);
            result = Status.ERROR;
        }

        return result;
    }

}