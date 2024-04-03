package hoan.com.springboot.models.entities;

import hoan.com.springboot.common.enums.TimeOffType;
import hoan.com.springboot.common.model.BaseCommonEntity;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author: Admin
 * @date: 6/27/2023
 **/
@Entity
@Table(name = "time_off", indexes = {
        @Index(name = "time_off_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class TimeOffEntity extends BaseCommonEntity {
    @Column(name = "employee_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    @Type(type = "uuid-char")
    private UUID employeeId;

    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeOffType type;

    @Column(name = "total", nullable = false)
    private Float total;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;
}
