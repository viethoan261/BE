package hoan.com.springboot.payload.request;

import hoan.com.springboot.common.enums.Gender;
import hoan.com.springboot.common.validator.ValidateConstraint;
import hoan.com.springboot.validation.annotation.UniqueEmail;
import hoan.com.springboot.validation.annotation.UniquePhoneNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * @author: Admin
 * @date: 6/24/2023
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserUpdateRequest extends Request {
    @NotBlank(message = "FULL_NAME_REQUIRED")
    @Size(max = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, message = "FULL_NAME_LENGTH")
    private String fullName;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Size(max = ValidateConstraint.LENGTH.EMAIL_MAX_LENGTH, message = "EMAIL_LENGTH")
    @Pattern(regexp = ValidateConstraint.FORMAT.EMAIL_PATTERN, message = "EMAIL_WRONG_FORMAT")
    private String email;

    @NotBlank(message = "PHONE_NUMBER_REQUIRED")
    @Size(max = ValidateConstraint.LENGTH.PHONE_MAX_LENGTH, message = "PHONE_NUMBER_LENGTH")
    @Pattern(regexp = ValidateConstraint.FORMAT.PHONE_NUMBER_PATTERN, message = "PHONE_NUMBER_FORMAT")
    private String phoneNumber;

    private Gender gender;

    @NotNull
    private List<String> roleIds;

    private String description;

    private LocalDate dayOfBirth;

    private String avatarFileId;

    private String departmentId;
}
