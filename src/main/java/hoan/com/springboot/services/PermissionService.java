package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.PermissionEntity;

import java.util.List;

public interface PermissionService {
    List<PermissionEntity> findAll();
}
