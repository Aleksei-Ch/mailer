package ru.mailserver.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final String REGEX = "(7|8)?\\d{10}";
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        var result = true;

        if (value != null && !value.isBlank()) {
            var val = value.replaceAll("[\\s-\\+\\()]", "");
            if (!val.matches(REGEX)) {
                result = false;
            }
        }

        return result;
    }

}