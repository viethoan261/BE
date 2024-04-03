package hoan.com.springboot.services;

import hoan.com.springboot.common.UserAuthority;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.ChangePwdRequest;
import hoan.com.springboot.payload.request.LoginRequest;
import hoan.com.springboot.payload.request.UserRegisterRequest;
import hoan.com.springboot.payload.response.UserResponse;

public interface AccountService {
    UserEntity register(UserRegisterRequest request);

    String login(LoginRequest request);

    UserAuthority myAuthorities();

    UserResponse getProfile();

    boolean changePwd(ChangePwdRequest request);

    boolean logout();
}
