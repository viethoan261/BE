package hoan.com.springboot.common.enums;

public enum TimeOffStatus {
    APPROVED("Approved"),
    PENDING("Pending"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    String value;

    TimeOffStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
