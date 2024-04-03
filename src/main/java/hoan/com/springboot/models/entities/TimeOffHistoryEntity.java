package hoan.com.springboot.models.entities;

import hoan.com.springboot.common.enums.EventHistory;
import hoan.com.springboot.common.enums.TimeOffType;
import hoan.com.springboot.common.model.BaseCommonEntity;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author: Admin
 * @date: 6/27/2023
 **/
@Entity
@Table(name = "time_off_history", indexes = {
        @Index(name = "time_off_history_idx", columnList = "deleted"),
        @Index(name = "time_off_history_employee_id_idx", columnList = "employee_id")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class TimeOffHistoryEntity extends BaseCommonEntity  {
    @Column(name = "employee_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    @Type(type = "uuid-char")
    private UUID employeeId;

    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeOffType type;

    @Column(name = "event", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventHistory event;

    @Column(name = "changed_by", length = ValidateConstraint.LENGTH.VALUE_MAX_LENGTH, nullable = false)
    private String changedBy;

    @Column(name = "change_days", nullable = false)
    private Float changeDays;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "request_id")
    @Type(type = "uuid-char")
    private UUID requestId;
}
