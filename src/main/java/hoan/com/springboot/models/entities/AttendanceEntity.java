package hoan.com.springboot.models.entities;

import hoan.com.springboot.common.model.BaseCommonEntity;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "attendance", indexes = {
        @Index(name = "attendance_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AttendanceEntity extends BaseCommonEntity {
    private LocalDateTime start;

    private LocalDateTime end;

    private String note;

    private Boolean deleted = Boolean.FALSE;

    private String employeeName;

    @Column(name = "employee_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    @Type(type = "uuid-char")
    private UUID employeeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttendanceEntity that = (AttendanceEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
