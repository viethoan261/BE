package hoan.com.springboot.common.enums;

/**
 * @author: Admin
 * @date: 6/27/2023
 **/
public enum EventHistory {
    TAKE_TIME_OFF("Take Time Off"),
    BALANCE_ADJUSTMENT("Balance Adjustment"),
    ACCRUAL("Accrual");

    String value;

    EventHistory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
