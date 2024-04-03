package hoan.com.springboot.models.entities;

import hoan.com.springboot.common.enums.TimeOffStatus;
import hoan.com.springboot.common.enums.TimeOffType;
import hoan.com.springboot.common.model.BaseCommonEntity;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "time_off_request", indexes = {
        @Index(name = "time_off_request_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class TimeOffRequestEntity extends BaseCommonEntity {
    @Column(name = "employee_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    @Type(type = "uuid-char")
    private UUID employeeId;

    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeOffType type;

    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeOffStatus status = TimeOffStatus.PENDING;

    @Column(name = "note", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH)
    private String note;

    @Column(name = "file_id", length = ValidateConstraint.LENGTH.FILE_NAME)
    private String fileId;

    @Column(name = "day_off", nullable = false)
    private Float dayOff;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "start")
    private long start;

    @Column(name = "end")
    private long end;
}
