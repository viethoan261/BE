package hoan.com.springboot.validation.annotation;

import hoan.com.springboot.validation.validator.UniquePhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UniquePhoneNumber {
    String message() default "The phone number already used.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
