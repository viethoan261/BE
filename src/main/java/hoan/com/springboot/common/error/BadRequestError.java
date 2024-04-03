package hoan.com.springboot.common.error;

import lombok.Getter;

import lombok.Getter;

@Getter
public enum BadRequestError implements ResponseError {
    INVALID_INPUT(40000001, "Invalid input : {0}"),
    INVALID_ACCEPT_LANGUAGE(40000002, "Invalid value for request header Accept-Language: {0}"),
    MISSING_PATH_VARIABLE(40000003, "Missing path variable"),
    PATH_INVALID(40000004, "Path is invalid"),
    UNDEFINED(40000005, ""),
    FILE_SIZE_EXCEEDED(40000006, "File size exceeds the limit"),
    CAN_NOT_APPROVE_WITH_APPROVAL_STATUS_OTHER_THAN_WAIT_APPROVE_STATUS(40000007, "Can not approve with approval status other than wait approve status"),
    CAN_NOT_REJECT_WITH_APPROVAL_STATUS_OTHER_THAN_WAIT_APPROVE_STATUS(40000008, "Can not reject with approval status other than wait approve status"),
    CAN_NOT_CANCEL_WITH_APPROVAL_STATUS_OTHER_THAN_WAIT_APPROVE_STATUS(40000009, "Can not cancel with approval status other than wait approve status"),
    CAN_NOT_WAIT_APPROVE_WITH_APPROVAL_STATUS_OTHER_THAN_NEW_STATUS(40000010, "Can not wait approve with approval status other than wait cancel status"),
    CAN_NOT_DELETE_IN_APPROVED_OR_WAIT_APPROVE(40000011, "Can not delete in approved or wait approve"),
    RECORD_IS_BEING_UPDATED(40000012, "The record is being updated. Please wait a minute"),
    LOGIN_FAIL_DUE_INACTIVE_ACCOUNT(40000013, "Login fail"),
    INVALID_ROLE(40000014, "Invalid role"),
    USER_NOT_FOUND(40000015, "User not found"),
    USER_NOT_ACTIVE(40000016, "User is not found"),
    DEPARTMENT_NOT_EXISTED(40000017, "Department is not existed"),
    PWD_INVALID(40000018, "Password is invalid"),
    EMAIL_INVALID(40000019, "Email is existed"),
    PHONE_INVALID(40000020, "Phone is existed"),
    NULL_POINTER(40000021, "Null pointer exception"),
    OUT_OFF_LEAVE_DAY(40000022, "Out off leave day"),
    DATE_INVALID(40000023, "Date invalid"),
    REQUEST_TIME_OFF_NOT_FOUND(40000024, "Time off request not found"),
    OPEN_USER_TEMPLATE_FAIL(40000025, "Open user template fail"),
    USER_SHEET_TEMPLATE_REQUIRED(40000026, "Users sheet is required"),
    DOWNLOAD_USER_TEMPLATE_FAIL(40000027, "Download user template fail"),
    OPEN_DEPARTMENT_TEMPLATE_FAIL(40000028, "Open department template fail"),
    DEPARTMENT_SHEET_TEMPLATE_REQUIRED(40000029, "Departments sheet is required"),
    DOWNLOAD_TEMPLATE_TEMPLATE_FAIL(40000030, "Download department template fail"),
    CHECKOUT_FAIL(40000031, "Can not checkout"),
    OPEN_ATTENDANCE_TEMPLATE_FAIL(40000032, "Open attendance template fail"),
    ATTENDANCE_SHEET_TEMPLATE_REQUIRED(40000033, "Attendance sheet is required"),
    NEWS_NOT_EXISTED(40000034, "News is not existed"),
    STATUS_NEWS_IS_INVALID(40000035, "News status is invalid")
    ;

    private final Integer code;
    private final String message;

    BadRequestError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}

