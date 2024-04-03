package hoan.com.springboot.controllers;

import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.payload.request.RoleAssignRequest;
import hoan.com.springboot.payload.request.RoleCreateOrUpdateRequest;
import hoan.com.springboot.payload.response.RoleResponse;
import hoan.com.springboot.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Create new role")
    @PostMapping("")
    @PreAuthorize("hasPermission(null, 'role:create')")
    public Response<RoleEntity> create(@RequestBody @Valid RoleCreateOrUpdateRequest request) {
        return Response.of(roleService.create(request));
    }

    @Operation(summary = "Update role")
    @PostMapping("{id}")
    @PreAuthorize("hasPermission(null, 'role:update')")
    public Response<RoleEntity> update(@PathVariable String id, @RequestBody @Valid RoleCreateOrUpdateRequest request) {
        return Response.of(roleService.update(id, request));
    }

    @Operation(summary = "Toggle role - active or inactive")
    @PostMapping("{id}/toggle")
    @PreAuthorize("hasPermission(null, 'role:update')")
    public Response<Boolean> toggle(@PathVariable String id) {
        return Response.of(roleService.toggleRole(id));
    }

    @Operation(summary = "Delete role")
    @PostMapping("{id}/delete")
    @PreAuthorize("hasPermission(null, 'role:delete')")
    public Response<Boolean> delete(@PathVariable String id) {
        return Response.of(roleService.delete(id));
    }

    @Operation(summary = "Assign permission")
    @PostMapping("{id}/assign-permission")
    @PreAuthorize("hasPermission(null, 'role:update')")
    public Response<Boolean> assignPermission(@PathVariable String id, @RequestBody RoleAssignRequest request) {
        return Response.of(roleService.assignPermission(id, request));
    }

    @Operation(summary = "Get all role")
    @GetMapping("")
    @PreAuthorize("hasPermission(null, 'role:view')")
    public Response<List<RoleResponse>> getAll() {
        return Response.of(roleService.getAll());
    }

    @Operation(summary = "Get detail role")
    @GetMapping("{id}")
    @PreAuthorize("hasPermission(null, 'role:view')")
    public Response<RoleResponse> detail(@PathVariable String id) {
        return Response.of(roleService.detail(id));
    }

    @Operation(summary = "Add user into role")
    @PostMapping("{id}/add-user")
    @PreAuthorize("hasPermission(null, 'role:update')")
    public Response<Boolean> addUser(@PathVariable String id, @RequestBody List<String> roleIds) {
        return Response.of(roleService.addUser(id, roleIds));
    }

    @Operation(summary = "Delete user from role")
    @PostMapping("{id}/delete-user")
    @PreAuthorize("hasPermission(null, 'role:update')")
    public Response<Boolean> deleteUser(@PathVariable String id, String userId) {
        return Response.of(roleService.deleteUser(id, userId));
    }
}
