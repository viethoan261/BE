package hoan.com.springboot;

import hoan.com.springboot.common.enums.ResourceCategory;
import hoan.com.springboot.common.enums.Scope;
import hoan.com.springboot.models.entities.PermissionEntity;
import hoan.com.springboot.repository.PermissionRepository;
import hoan.com.springboot.services.impl.SessionDestroyedEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
//@EnableSpringHttpSession
public class SpringbootApplication implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // lấy tất cả active permissions trong db
        List<PermissionEntity> activePermissionEntities = this.permissionRepository.findAllActivated();

        // lấy tất cả resources
        ResourceCategory[] resourceCategories = ResourceCategory.values();

        List<PermissionEntity> permissions = new ArrayList<>();
        List<PermissionEntity> permissionsUpdate = new ArrayList<>();
        for (ResourceCategory resource : resourceCategories) {
            if (CollectionUtils.isEmpty(resource.getScopes())) {
                continue;
            }
            for (Scope scope : resource.getScopes()) {
                Optional<PermissionEntity> optionalPermissionEntity = activePermissionEntities.stream()
                        .filter(p -> p.getResourceCode().equals(resource.getResourceCode())
                                && p.getScope().equals(scope)).findFirst();
                if (optionalPermissionEntity.isPresent()) {
                    if (Objects.isNull(optionalPermissionEntity.get().getPriority())) {
                        PermissionEntity permissionDomainUpdate = optionalPermissionEntity.get();
                        permissionDomainUpdate.setPriority(resource.getPriority());
                        permissionsUpdate.add(permissionDomainUpdate);
                    }
                    continue;
                }

                String scopeName = this.getScopeName(resource, scope);

                PermissionEntity permission = new PermissionEntity(scope, resource.getResourceCode(), scopeName, Boolean.FALSE, resource.getPriority(), null);

                permissions.add(permission);
            }
        }

        List<PermissionEntity> permissionEntities = permissions;
        this.permissionRepository.saveAll(permissionEntities);

        // Update priority for available permissions
        List<PermissionEntity> permissionUpdateEntities = permissionsUpdate;
        this.permissionRepository.saveAll(permissionUpdateEntities);
    }

    private String getScopeName(ResourceCategory resource, Scope scope) {
        String scopeName = "Xem";
        if (Scope.VIEW.equals(scope)) {
            scopeName = "Xem";
        } else if (Scope.REPORT.equals(scope)) {
            scopeName = "Báo cáo";
        } else if (Scope.CREATE.equals(scope)) {
            scopeName = "Tạo";
        } else if (Scope.UPDATE.equals(scope)) {
            scopeName = "Cập nhật";
        } else if (Scope.DELETE.equals(scope)) {
            scopeName = "Xóa";
        } else if (Scope.REVIEW.equals(scope)) {
            scopeName = "Tiếp nhận";
        } else if (Scope.APPROVE.equals(scope)) {
            scopeName = "Phê duyệt";
        } else if (Scope.EXPORT.equals(scope)) {
            scopeName = "Xuất báo cáo";
        }

        return scopeName;
    }

    @Bean
    public SessionDestroyedEventListener sessionDestroyedEventListener(SessionRegistry sessionRegistry) {
        return new SessionDestroyedEventListener(sessionRegistry);
    }
}
