package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.UserUpdateRequest;
import hoan.com.springboot.payload.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse update(String id, UserUpdateRequest request);

    UserResponse getById(String id);

    void exportToExcel(HttpServletResponse response);
}
