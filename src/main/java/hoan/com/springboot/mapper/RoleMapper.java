package hoan.com.springboot.mapper;

import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.payload.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleResponse from(RoleEntity entity);
}
