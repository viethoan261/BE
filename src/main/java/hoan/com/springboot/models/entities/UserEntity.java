package hoan.com.springboot.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hoan.com.springboot.common.enums.Gender;
import hoan.com.springboot.common.enums.UserLevel;
import hoan.com.springboot.common.enums.UserStatus;
import hoan.com.springboot.common.model.BaseCommonEntity;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "user_username_idx", columnList = "username"),
        @Index(name = "user_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseCommonEntity {

    @Column(name = "username", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = ValidateConstraint.LENGTH.VALUE_MAX_LENGTH)
    private String password;

    @Column(name = "full_name", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String fullName;

    @Column(name = "email", length = ValidateConstraint.LENGTH.EMAIL_MAX_LENGTH, nullable = false)
    private String email;

    @Column(name = "phone_number", length = ValidateConstraint.LENGTH.PHONE_MAX_LENGTH, nullable = false)
    private String phoneNumber;

    @Column(name = "day_of_birth")
    private LocalDate dayOfBirth;

    @Column(name = "gender", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.OTHER;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "employee_code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    private String employeeCode;

    @Column(name = "title", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH)
    private String title;

    @Column(name = "department_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    @Type(type = "uuid-char")
    private UUID departmentId;

    @Column(name = "description", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH)
    private String description;

    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "avatar_file_id")
    private String avatarFileId;

    @Column(name = "user_level", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
