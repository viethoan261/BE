package hoan.com.springboot.services;

import hoan.com.springboot.common.enums.TimeOffStatus;
import hoan.com.springboot.models.entities.TimeOffRequestEntity;
import hoan.com.springboot.payload.request.TimeOffRequest;
import hoan.com.springboot.payload.response.TimeoffRequestResponse;

import java.util.List;
import java.util.UUID;

public interface TimeOffRequestService {
    TimeOffRequestEntity create(TimeOffRequest request);

    List<TimeOffRequestEntity> myRequest();

    List<TimeoffRequestResponse> getAll();

    boolean changeRequestStatus(UUID requestId, TimeOffStatus status);
}
