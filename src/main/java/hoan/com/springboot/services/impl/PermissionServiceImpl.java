package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.enums.ResourceCategory;
import hoan.com.springboot.config.i18n.LocaleStringService;
import hoan.com.springboot.models.entities.PermissionEntity;
import hoan.com.springboot.repository.PermissionRepository;
import hoan.com.springboot.services.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    private final LocaleStringService localeStringService;

    @Override
    public List<PermissionEntity> findAll() {
        List<PermissionEntity> permissionEntities = this.permissionRepository.findAllActivated();

        ResourceCategory[] resourceCategories = ResourceCategory.values();

        permissionEntities.forEach(p -> {
            for (ResourceCategory perType : resourceCategories) {
                if (p.getResourceCode().equals(perType.getResourceCode())) {
                    p.setResourceName(this.localeStringService.getMessage(perType.getResourceName(), perType.getResourceName()));
                }
            }
            if (!StringUtils.hasLength(p.getResourceName())) {
                p.setResourceName(p.getResourceCode());
            }
        });

        return permissionEntities;
    }
}
