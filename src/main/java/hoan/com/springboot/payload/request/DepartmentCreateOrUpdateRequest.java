package hoan.com.springboot.payload.request;

import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentCreateOrUpdateRequest extends Request{

    @NotBlank(message = "DEPARTMENT_NAME_REQUIRED")
    @Size(max = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, message = "DEPARTMENT_NAME_LENGTH")
    private String name;

    @Size(max = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, message = "DEPARTMENT_DESCRIPTION_LENGTH")
    private String description;

    @Size(max = ValidateConstraint.LENGTH.ID_MAX_LENGTH, message = "ID_MAX_LENGTH")
    private String parentId;
}
