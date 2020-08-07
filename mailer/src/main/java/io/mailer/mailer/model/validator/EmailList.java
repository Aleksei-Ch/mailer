package io.mailer.mailer.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = EmailListValidator.class)
@Documented
public @interface EmailList {

    String message() default "Incorrect email list. Use list of emails separated by ; or ,";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}