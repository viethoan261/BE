package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.payload.request.RoleAssignRequest;
import hoan.com.springboot.payload.request.RoleCreateOrUpdateRequest;
import hoan.com.springboot.payload.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleEntity create(RoleCreateOrUpdateRequest request);

    RoleEntity update(String id, RoleCreateOrUpdateRequest request);

    Boolean toggleRole(String id);

    Boolean delete(String id);

    Boolean assignPermission(String id, RoleAssignRequest request);

    List<RoleResponse> getAll();

    RoleResponse detail(String id);

    boolean addUser(String roleId, List<String> userIds);

    boolean deleteUser(String roleId, String userId);
}
