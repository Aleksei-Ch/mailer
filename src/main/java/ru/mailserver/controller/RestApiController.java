package ru.mailserver.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mailserver.email.EmailQueue;
import ru.mailserver.model.EmailRequest;
import ru.mailserver.model.GlobalResponse;
import ru.mailserver.model.SmsRequest;
import ru.mailserver.sms.SmsSender;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@OpenAPIDefinition(info = @Info(title = "Mail Server", version = "v1.0.0"))
@SecurityScheme(
        name = "ApiKey",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization",
        type = SecuritySchemeType.APIKEY,
        scheme = "Authorization"
)
public class RestApiController {

    private static final Logger LOG = LoggerFactory.getLogger(RestApiController.class);

    private final EmailQueue emailQueue;
    private final SmsSender smsSender;

    public RestApiController(EmailQueue emailQueue, SmsSender smsSender) {
        this.emailQueue = emailQueue;
        this.smsSender = smsSender;
    }

    @Operation(summary = "Send an email", security = @SecurityRequirement(name = "ApiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Incorrect request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))})
    })
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public GlobalResponse send(@Valid @RequestBody EmailRequest req) {

        var response = GlobalResponse.builder();

        try {
            // Prepare
            req.setFrom(req.getFrom().replaceAll(",", ";"));
            req.setTo(req.getTo().replaceAll(",", ";"));
            if (req.getCc() != null) req.setCc(req.getCc().replaceAll(",", ";"));
            if (req.getBcc() != null) req.setBcc(req.getBcc().replaceAll(",", ";"));

            // Send
            if (!emailQueue.addMessage(req)) {
                throw new Exception("Cannot add message to the queue.");
            }

            response.payload("Message sent");
        } catch (Exception e) {
            LOG.error("An exception occurred: ", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).payload(List.of(e.getMessage()));
        }

        return response.build();

    }

    @Operation(summary = "Send an sms", security = @SecurityRequirement(name = "ApiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Incorrect request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))})
    })
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public GlobalResponse sendSms(@Valid @RequestBody SmsRequest req) {

        var response = GlobalResponse.builder();

        try {
            // Prepare
            req.setTo(req.getTo().replaceAll("[\\s-\\+\\()]", ""));

            // Send
            var id = smsSender.send(req);

            response.payload(id);
        } catch (Exception e) {
            LOG.error("An exception occurred: ", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).payload(List.of(e.getMessage()));
        }

        return response.build();

    }

    @Operation(summary = "Get an sms status by its id", security = @SecurityRequirement(name = "ApiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status placed in response 'payload'",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Incorrect message id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))})
    })
    @RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
    public GlobalResponse getSmsStatus(@PathVariable String id) {

        var response = GlobalResponse.builder();

        try {
            if (id == null || !id.matches("^\\d+$")) {
                response.status(HttpStatus.BAD_REQUEST).payload("Incorrect message ID");
            }

            // Get status
            var status = smsSender.getStatus(id);

            response.payload(status);
        } catch (Exception e) {
            LOG.error("An exception occurred: ", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR).payload(List.of(e.getMessage()));
        }

        return response.build();

    }

}