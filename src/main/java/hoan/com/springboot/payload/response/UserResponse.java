package hoan.com.springboot.payload.response;

import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.models.entities.UserEntity;
import lombok.Data;

import java.util.List;

/**
 * @author: Admin
 * @date: 6/24/2023
 **/
@Data
public class UserResponse extends UserEntity {
    private List<RoleEntity> roles;
}
