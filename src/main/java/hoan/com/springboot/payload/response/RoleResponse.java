package hoan.com.springboot.payload.response;

import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.models.entities.RolePermissionEntity;
import hoan.com.springboot.models.entities.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class RoleResponse extends RoleEntity {
    private List<String> properties;

    private List<RolePermissionEntity> permissions;

    private List<UserEntity> users;
}
