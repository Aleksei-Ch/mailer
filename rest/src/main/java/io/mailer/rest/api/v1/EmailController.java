package io.mailer.rest.api.v1;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mailer.rest.model.EmailRequest;
import io.mailer.rest.model.GlobalResponse;
import io.mailer.rest.rpc.EmailSenderRpcClient;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    @Autowired
    private EmailSenderRpcClient rpcClient;

    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    @RequestMapping("/send")
    public GlobalResponse send(@Valid @RequestBody EmailRequest req) throws Exception {

        var response = GlobalResponse.builder();

        try {

            // Prepare
            req.setFrom(req.getFrom().replaceAll(",", ";"));
            req.setTo(req.getTo().replaceAll(",", ";"));
            if (req.getCc() != null) req.setCc(req.getCc().replaceAll(",", ";"));
            if (req.getBcc() != null) req.setBcc(req.getBcc().replaceAll(",", ";"));

            // Send
            var result = rpcClient.exchange(req);

            response.payload(result);

        } catch (Exception e) {
            LOG.error("An exception occurred: ", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).errors(List.of(e.getMessage()));
        }
        return response.build();
    }

    @RequestMapping("/sendAsync")
    public GlobalResponse sendAsync(@Valid @RequestBody EmailRequest req) throws Exception {

        var response = GlobalResponse.builder();

        try {

            // Prepare
            req.setFrom(req.getFrom().replaceAll(",", ";"));
            req.setTo(req.getTo().replaceAll(",", ";"));
            if (req.getCc() != null) req.setCc(req.getCc().replaceAll(",", ";"));
            if (req.getBcc() != null) req.setBcc(req.getBcc().replaceAll(",", ";"));

            // Send
            new Thread(() -> {
                try {
                    var result = rpcClient.exchange(req);
                    LOG.info("Sending result: " + result);
                } catch (Exception e) {
                    LOG.error("An exception occurred : ", e);
                }
            }).start();

        } catch (Exception e) {
            LOG.error("An exception occurred: ", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).errors(List.of(e.getMessage()));
        }
        return response.build();
    }

}