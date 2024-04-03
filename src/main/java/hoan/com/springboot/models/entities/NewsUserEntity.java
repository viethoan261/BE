package hoan.com.springboot.models.entities;

import hoan.com.springboot.common.model.BaseCommonEntity;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "news_user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewsUserEntity extends BaseCommonEntity {
    @Column(name = "employee_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    @Type(type = "uuid-char")
    private UUID employeeId;

    @Column(name = "news_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    @Type(type = "uuid-char")
    private UUID newsId;
}
