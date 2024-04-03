package hoan.com.springboot.payload.request;

import hoan.com.springboot.common.enums.Scope;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionCreateRequest extends Request{

    @Size(max = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, message = "ROLE_PERMISSION_RESOURCE_LENGTH")
    private String resourceCode;

    private List<Scope> scopes;
}
