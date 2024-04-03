package hoan.com.springboot.common.enums;

public enum TimeOffType {
    ANNUAL("Annual"),
    UNPAID_TIME_OFF("Unpaid time off"),
    WEDDING("Wedding");

    String value;

    TimeOffType(String value) {
        this.value = value;
    }

    public String getValue(TimeOffType type) {
        return value;
    }
}
