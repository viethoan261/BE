package hoan.com.springboot.mapper;

import hoan.com.springboot.models.entities.DepartmentEntity;
import hoan.com.springboot.payload.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentResponse from(DepartmentEntity entity);

    List<DepartmentResponse> from(List<DepartmentEntity> entityList);
}
