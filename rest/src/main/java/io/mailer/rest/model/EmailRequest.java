package io.mailer.rest.model;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.mailer.rest.model.validator.EmailList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    @NotBlank(message = "Parameter 'from' can not be blank")
    @EmailList(message = "Incorrect email list in 'from' parameter. Use list of emails separated by ; or ,")
    private String from;

    @NotBlank(message = "Parameter 'to' can not be blank")
    @EmailList(message = "Incorrect email list in 'to' parameter. Use list of emails separated by ; or ,")
    private String to;

    @EmailList(message = "Incorrect email list in 'cc' parameter. Use list of emails separated by ; or ,")
    private String cc;

    @EmailList(message = "Incorrect email list in 'bcc' parameter. Use list of emails separated by ; or ,")
    private String bcc;

    @NotBlank(message = "Title can not be blank")
    private String title;

    @NotBlank(message = "Message body can not be blank")
    private String body;

    private Map<String, byte[]> attachments;

    private Map<String, Object> params;

}