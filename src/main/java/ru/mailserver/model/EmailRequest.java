package ru.mailserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mailserver.model.validation.EmailList;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    @NotBlank(message = "Parameter 'from' can not be blank")
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

}
