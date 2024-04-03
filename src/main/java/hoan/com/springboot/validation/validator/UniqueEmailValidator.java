package hoan.com.springboot.validation.validator;

import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.repository.UserRepository;
import hoan.com.springboot.validation.annotation.UniqueEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;

    private String message;

    @Override
    public void initialize(UniqueEmail uniqueEmail) {
        message = uniqueEmail.message();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null)
            return false;

        Optional<UserEntity> userOtp = userRepository.findByEmail(email);

        if (userOtp.isEmpty()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
