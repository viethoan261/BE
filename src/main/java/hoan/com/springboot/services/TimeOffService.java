package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.TimeOffEntity;

import java.util.List;

public interface TimeOffService {
    List<TimeOffEntity> getMyTimeOff();
}
