package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.TimeOffHistoryEntity;

import java.util.List;

public interface TimeOffHistoryService {
    List<TimeOffHistoryEntity> getAll();
}
