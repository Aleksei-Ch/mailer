package io.mailer.rest.api.v1;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mailer.rest.model.EmailRequest;
import io.mailer.rest.model.GlobalResponse;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    @RequestMapping("/send")
    public GlobalResponse send(@Valid @RequestBody EmailRequest req) throws Exception {
        LOG.debug("Got message: " + req);
        return GlobalResponse.builder().payload(req.getTitle()).build();
    }

}