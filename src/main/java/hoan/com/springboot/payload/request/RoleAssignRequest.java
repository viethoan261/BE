package hoan.com.springboot.payload.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAssignRequest extends Request {

    private List<String> permissionIds;
}
