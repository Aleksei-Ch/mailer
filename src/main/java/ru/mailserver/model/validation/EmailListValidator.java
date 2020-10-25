package ru.mailserver.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailListValidator implements ConstraintValidator<EmailList, String> {

    private static final String REGEX = "(?im)^(?=.{1,64}@)(?:(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"@)|((?:[0-9a-z](?:\\.(?!\\.)|[-!#\\$%&'\\*\\+/=\\?\\^`\\{\\}\\|~\\w])*)?[0-9a-z]@))(?=.{1,255}$)(?:(\\[(?:\\d{1,3}\\.){3}\\d{1,3}\\])|((?:(?=.{1,63}\\.)[0-9a-z][-\\w]*[0-9a-z]*\\.)+[a-z0-9][\\-a-z0-9]{0,22}[a-z0-9])|((?=.{1,63}$)[0-9a-z][-\\w]*))$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        var result = true;

        if (value != null && !value.isBlank()) {
            for (var email : value.replaceAll(",", ";").split(";")) {
                if (!email.matches(REGEX)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

}