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

        GlobalResponse response = null;

        try {

            // Prepare
            req.setFrom(req.getFrom().replaceAll(",", ";"));
            req.setTo(req.getTo().replaceAll(",", ";"));
            if (req.getCc() != null) req.setCc(req.getCc().replaceAll(",", ";"));
            if (req.getBcc() != null) req.setBcc(req.getBcc().replaceAll(",", ";"));

            // Send
            var result = rpcClient.exchange(req);

            response = GlobalResponse.builder().payload(result).build();

        } catch (Exception e) {
            LOG.error("An exception occurred: ", e);
            response = GlobalResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errors(List.of(e.getMessage()))
                .build();
        }
        return response;
    }

}