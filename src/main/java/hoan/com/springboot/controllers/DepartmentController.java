package hoan.com.springboot.controllers;

import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.DepartmentEntity;
import hoan.com.springboot.payload.request.DepartmentCreateOrUpdateRequest;
import hoan.com.springboot.payload.response.DepartmentResponse;
import hoan.com.springboot.services.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Create new department")
    @PostMapping("")
    @PreAuthorize("hasPermission(null, 'department:create')")
    public Response<DepartmentEntity> create(@RequestBody @Valid DepartmentCreateOrUpdateRequest request) {
        return Response.of(departmentService.create(request));
    }

    @Operation(summary = "Update department")
    @PostMapping("{id}")
    @PreAuthorize("hasPermission(null, 'department:update')")
    public Response<DepartmentEntity> update(@PathVariable String id, @RequestBody @Valid DepartmentCreateOrUpdateRequest request) {
        return Response.of(departmentService.update(id, request));
    }

    @Operation(summary = "Delete department")
    @PostMapping("{id}/delete")
    @PreAuthorize("hasPermission(null, 'department:delete')")
    public Response<Boolean> delete(@PathVariable String id) {
        return Response.of(departmentService.delete(id));
    }

    @Operation(summary = "Get all department")
    @GetMapping("")
    @PreAuthorize("hasPermission(null, 'department:view')")
    public Response<List<DepartmentResponse>> getAll() {
        return Response.of(departmentService.getAll());
    }

    @Operation(summary = "Detail department")
    @GetMapping("{id}")
    @PreAuthorize("hasPermission(null, 'department:view')")
    public Response<DepartmentResponse> getAll(@PathVariable String id) {
        return Response.of(departmentService.detail(id));
    }

    @Operation(summary = "Delete user from department")
    @PostMapping("{id}/delete-user")
    @PreAuthorize("hasPermission(null, 'department:update')")
    public Response<Boolean> deleteUser(@PathVariable String id, String userId) {
        return Response.of(departmentService.deleteUser(id, userId));
    }

    @Operation(summary = "Add user into department")
    @PostMapping("{id}/add-user")
    @PreAuthorize("hasPermission(null, 'department:update')")
    public Response<Boolean> addUser(@PathVariable String id, @RequestBody List<String> userIds) {
        return Response.of(departmentService.addUser(id, userIds));
    }

    @Operation(summary = "Export department to excel")
    @GetMapping("export")
    @PreAuthorize("hasPermission(null, 'department:view')")
    public void export(HttpServletResponse response) {
        departmentService.exportToExcel(response);
    }
}
