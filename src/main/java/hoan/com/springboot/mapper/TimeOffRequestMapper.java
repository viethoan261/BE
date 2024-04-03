package hoan.com.springboot.mapper;

import hoan.com.springboot.models.entities.TimeOffRequestEntity;
import hoan.com.springboot.payload.request.TimeOffRequest;
import hoan.com.springboot.payload.response.TimeoffRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TimeOffRequestMapper {
    TimeOffRequestMapper INSTANCE = Mappers.getMapper(TimeOffRequestMapper.class);

    TimeOffRequestEntity from(TimeOffRequest request);

    TimeoffRequestResponse from(TimeOffRequestEntity entity);
}
