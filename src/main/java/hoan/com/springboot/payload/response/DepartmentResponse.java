package hoan.com.springboot.payload.response;

import hoan.com.springboot.models.entities.DepartmentEntity;
import hoan.com.springboot.models.entities.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentResponse extends DepartmentEntity {
    private List<UserEntity> users;
}
