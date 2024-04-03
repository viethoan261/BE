package hoan.com.springboot.common.enums;

import java.util.List;

public enum ResourceCategory {
    ROLE_MANAGEMENT("ROLE", "ROLE_MANAGEMENT_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 1),
    USER_MANAGEMENT("USER", "USER_MANAGEMENT_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 2),
    DEPARTMENT_MANAGEMENT("DEPARTMENT", "DEPARTMENT_MANAGEMENT_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 3),
    PERMISSION_MANAGEMENT("PERMISSION", "PERMISSION_MANAGEMENT_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 4),
    TIME_OFF_MANAGEMENT("TIMEOFF", "TIME_OFF_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 5),
    NEWS_MANAGEMENT("NEWS", "NEWS_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 6),
    SESSION_MANAGEMENT("SESSION", "SESSION_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 7),
    ATTENDANCE_MANAGEMENT("ATTENDANCE", "ATTENDANCE_TITLE", List.of(Scope.CREATE, Scope.VIEW, Scope.UPDATE, Scope.DELETE), 8)
    ;
    String resourceCode;
    String resourceName;
    Integer priority;
    List<Scope> scopes;

    ResourceCategory(String resourceCode, String resourceName, List<Scope> scopes, Integer priority) {
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.scopes = scopes;
        this.priority = priority;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public Integer getPriority() {
        return priority;
    }
}
