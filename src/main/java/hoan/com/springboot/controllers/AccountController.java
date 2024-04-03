package hoan.com.springboot.controllers;

import hoan.com.springboot.common.UserAuthority;
import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.ChangePwdRequest;
import hoan.com.springboot.payload.request.LoginRequest;
import hoan.com.springboot.payload.request.UserRegisterRequest;
import hoan.com.springboot.payload.request.UserUpdateRequest;
import hoan.com.springboot.payload.response.UserResponse;
import hoan.com.springboot.services.AccountService;
import hoan.com.springboot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Account resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class AccountController {

    private final AccountService accountService;

    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Operation(summary = "Register - Only for dev ")
    @PostMapping("/register")
    public Response<UserEntity> register(@RequestBody @Valid UserRegisterRequest request) {
        return Response.of(accountService.register(request));
    }

    @Operation(summary = "Login")
    @PostMapping("/authenticate")
    public Response<String> register(@RequestBody @Valid LoginRequest request) {
        return Response.of(accountService.login(request));
    }

    @Operation(summary = "Get my authorities")
    @GetMapping("/me/authorities")
    public Response<UserAuthority> myAuthorities() {
        UserAuthority userAuthority = this.accountService.myAuthorities();

        return Response.of(userAuthority);
    }

    @Operation(summary = "Get my profile")
    @GetMapping("/me")
    public Response<UserResponse> myProfile() {
        UserResponse user = this.accountService.getProfile();

        return Response.of(user);
    }

    @Operation(summary = "Update my profile")
    @PostMapping("/me/{id}")
    public Response<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserUpdateRequest request) {
        return Response.of(userService.update(id, request));
    }

    @Operation(summary = "change my password")
    @PostMapping("/me/change-pwd")
    public Response<Boolean> changePwd(@Valid @RequestBody ChangePwdRequest request) {
        return Response.of(accountService.changePwd(request));
    }

    @Operation(summary = "logout")
    @PostMapping("/me/logout")
    public Response<Boolean> logout() {
        return Response.of(accountService.logout());
    }
}
