package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.models.entities.TimeOffHistoryEntity;
import hoan.com.springboot.repository.TimeOffHistoryRepository;
import hoan.com.springboot.security.utils.SecurityUtils;
import hoan.com.springboot.services.TimeOffHistoryService;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TimeOffHistoryServiceImpl implements TimeOffHistoryService {
    private final TimeOffHistoryRepository timeOffHistoryRepository;

    public TimeOffHistoryServiceImpl(TimeOffHistoryRepository timeOffHistoryRepository) {
        this.timeOffHistoryRepository = timeOffHistoryRepository;
    }

    @Override
    public List<TimeOffHistoryEntity> getAll() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        UUID employeeId = UUID.fromString(loginId.get());

        List<TimeOffHistoryEntity> timeOffHistoryEntities = timeOffHistoryRepository.findAllByEmployeeId(employeeId);

        return timeOffHistoryEntities;
    }
}
