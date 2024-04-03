package hoan.com.springboot.validation.annotation;

import hoan.com.springboot.validation.validator.UniqueUserNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUserNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UniqueUserName {
    String message() default "The username already used.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
