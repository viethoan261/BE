package hoan.com.springboot.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseCommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    protected UUID id;

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected Instant createdAt = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    protected String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    protected Instant lastModifiedAt = Instant.now();
}
