package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.models.entities.TimeOffEntity;
import hoan.com.springboot.repository.TimeOffRepository;
import hoan.com.springboot.security.utils.SecurityUtils;
import hoan.com.springboot.services.TimeOffService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimeOffServiceImpl implements TimeOffService {
    private final TimeOffRepository timeOffRepository;

    public TimeOffServiceImpl(TimeOffRepository timeOffRepository) {
        this.timeOffRepository = timeOffRepository;
    }

    @Override
    public List<TimeOffEntity> getMyTimeOff() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        return timeOffRepository.findAll().stream().filter(t -> t.getEmployeeId().equals(UUID.fromString(loginId.get())))
                .collect(Collectors.toList());
    }
}
