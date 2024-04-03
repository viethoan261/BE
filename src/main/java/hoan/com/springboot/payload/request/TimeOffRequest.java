package hoan.com.springboot.payload.request;

import hoan.com.springboot.common.enums.TimeOffType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class TimeOffRequest extends Request {
    @NotNull(message = "TYPE_REQUIRED")
    private TimeOffType type;

    @NotNull(message = "DATE_REQUIRED")
    private LocalDate dateFrom;

    @NotNull(message = "DATE_REQUIRED")
    private LocalDate dateTo;

    private long start;

    private long end;

    @Min(value = 0)
    private float dayOff;

    private String note;

    private String fileId;
}
