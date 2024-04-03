package hoan.com.springboot.controllers;

import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.payload.request.UserUpdateRequest;
import hoan.com.springboot.payload.response.UserResponse;
import hoan.com.springboot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all user")
    @GetMapping("")
    @PreAuthorize("hasPermission(null, 'user:view')")
    public Response<List<UserResponse>> getAll() {
        return Response.of(userService.findAll());
    }

    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    @PreAuthorize("hasPermission(null, 'user:view')")
    public Response<UserResponse> getById(@PathVariable String id) {
        return Response.of(userService.getById(id));
    }

    @Operation(summary = "Update user by id")
    @PostMapping("{id}")
    @PreAuthorize("hasPermission(null, 'user:update')")
    public Response<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserUpdateRequest request) {
        return Response.of(userService.update(id, request));
    }

    @Operation(summary = "Export user to excel")
    @GetMapping("export")
    @PreAuthorize("hasPermission(null, 'user:view')")
    public void export(HttpServletResponse response) {
        userService.exportToExcel(response);
    }
}
