package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.enums.EventHistory;
import hoan.com.springboot.common.enums.TimeOffStatus;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.mapper.TimeOffRequestMapper;
import hoan.com.springboot.models.entities.TimeOffEntity;
import hoan.com.springboot.models.entities.TimeOffHistoryEntity;
import hoan.com.springboot.models.entities.TimeOffRequestEntity;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.TimeOffRequest;
import hoan.com.springboot.payload.response.TimeoffRequestResponse;
import hoan.com.springboot.repository.TimeOffHistoryRepository;
import hoan.com.springboot.repository.TimeOffRepository;
import hoan.com.springboot.repository.TimeOffRequestRepository;
import hoan.com.springboot.repository.UserRepository;
import hoan.com.springboot.security.utils.SecurityUtils;
import hoan.com.springboot.services.TimeOffRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TimeOffRequestServiceImpl implements TimeOffRequestService {
    private final TimeOffRequestRepository timeOffRequestRepository;

    private final UserRepository userRepository;

    private final TimeOffHistoryRepository timeOffHistoryRepository;

    private final TimeOffRepository timeOffRepository;

    public TimeOffRequestServiceImpl(TimeOffRequestRepository timeOffRequestRepository, UserRepository userRepository, TimeOffHistoryRepository timeOffHistoryRepository, TimeOffRepository timeOffRepository) {
        this.timeOffRequestRepository = timeOffRequestRepository;
        this.userRepository = userRepository;
        this.timeOffHistoryRepository = timeOffHistoryRepository;
        this.timeOffRepository = timeOffRepository;
    }

    @Override
    @Transactional
    public TimeOffRequestEntity create(TimeOffRequest request) {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        if (request.getDateFrom().isAfter(request.getDateTo())) {
            throw new ResponseException(BadRequestError.DATE_INVALID);
        }

        UserEntity user = userRepository.findById(UUID.fromString(loginId.get())).get();

        TimeOffRequestEntity timeOffRequestEntity = TimeOffRequestMapper.INSTANCE.from(request);

        timeOffRequestEntity.setEmployeeId(UUID.fromString(loginId.get()));

        timeOffRequestRepository.save(timeOffRequestEntity);

        //update leave day
        Optional<TimeOffEntity> timeOffEntityOpt = timeOffRepository.findByIdAndType(user.getId(), request.getType());

        if (timeOffEntityOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.OUT_OFF_LEAVE_DAY);
        }

        TimeOffEntity timeOffEntity = timeOffEntityOpt.get();

        timeOffEntity.setTotal(timeOffEntity.getTotal() - request.getDayOff());

        timeOffRepository.save(timeOffEntity);

        //update time off history
        TimeOffHistoryEntity timeOffHistoryEntity = new TimeOffHistoryEntity();

        timeOffHistoryEntity.setDate(request.getDateTo());
        timeOffHistoryEntity.setType(request.getType());
        timeOffHistoryEntity.setChangedBy(user.getFullName());
        timeOffHistoryEntity.setChangeDays(-1 * request.getDayOff());
        timeOffHistoryEntity.setEmployeeId(user.getId());
        timeOffHistoryEntity.setEvent(EventHistory.TAKE_TIME_OFF);
        timeOffHistoryEntity.setRequestId(timeOffRequestEntity.getId());

        timeOffHistoryRepository.save(timeOffHistoryEntity);

        return timeOffRequestEntity;
    }

    @Override
    public List<TimeOffRequestEntity> myRequest() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        return timeOffRequestRepository.myRequest(UUID.fromString(loginId.get()));
    }

    @Override
    public List<TimeoffRequestResponse> getAll() {
        List<TimeOffRequestEntity> timeOffRequestEntities = timeOffRequestRepository.getAll();

        List<TimeoffRequestResponse> res = new ArrayList<>();

        for (TimeOffRequestEntity entity : timeOffRequestEntities) {
            TimeoffRequestResponse response = TimeOffRequestMapper.INSTANCE.from(entity);
            response.setEmployeeName(userRepository.findById(entity.getEmployeeId()).get().getFullName());

            res.add(response);
        }

        return res;
    }

    @Override
    @Transactional
    public boolean changeRequestStatus(UUID requestId, TimeOffStatus status) {
        Optional<TimeOffRequestEntity> timeOffRequestOtp = timeOffRequestRepository.findById(requestId);

        if (timeOffRequestOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.REQUEST_TIME_OFF_NOT_FOUND);
        }

        TimeOffRequestEntity timeOffRequestEntity = timeOffRequestOtp.get();

        timeOffRequestEntity.setStatus(status);

        // revert day off and delete history balance
        if (TimeOffStatus.CANCELLED.equals(status) || TimeOffStatus.REJECTED.equals(status)) {
            TimeOffEntity timeOffEntity = timeOffRepository
                    .findByIdAndType(timeOffRequestEntity.getEmployeeId(), timeOffRequestEntity.getType()).get();

            TimeOffHistoryEntity timeOffHistoryEntity = timeOffHistoryRepository.findByRequestId(requestId).get();

            timeOffEntity.setTotal(timeOffEntity.getTotal() - timeOffHistoryEntity.getChangeDays());
            timeOffRepository.save(timeOffEntity);

            timeOffHistoryRepository.delete(timeOffHistoryEntity);
        }

        timeOffRequestRepository.save(timeOffRequestEntity);

        return true;
    }
}
