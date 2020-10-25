package ru.mailserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mailserver.model.validation.PhoneNumber;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {

    @NotBlank(message = "Parameter 'to' can not be blank")
    @PhoneNumber(message = "Incorrect recipient phone number")
    private String to;

    @NotBlank(message = "Message body can not be blank")
    private String body;

}
