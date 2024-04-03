package hoan.com.springboot.mapper;

import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.UserUpdateRequest;
import hoan.com.springboot.payload.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: Admin
 * @date: 6/24/2023
 **/
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse from(UserEntity entity);

    @Mapping(target="departmentId", expression = "java(map(request.getDepartmentId() == null ? null : request.getDepartmentId()))")
    UserEntity from(UserUpdateRequest request, @MappingTarget UserEntity userEntity);

    default UUID map(String value) {

        if (Objects.isNull(value)) {
            return null;
        }

        return UUID.fromString(value);
    }

    List<UserResponse> from(List<UserEntity> entities);
}
