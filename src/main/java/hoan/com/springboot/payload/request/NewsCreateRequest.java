package hoan.com.springboot.payload.request;

import hoan.com.springboot.common.enums.NewsStatus;
import hoan.com.springboot.common.validator.ValidateConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewsCreateRequest extends Request {
    @NotBlank(message = "TITLE_REQUIRED")
    @Size(max = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, message = "TITLE_LENGTH")
    private String title;

    @Size(max = ValidateConstraint.LENGTH.CONTENT_MAX_LENGTH,
            message = "CONTENT_LENGTH")
    @NotBlank(message = "CONTENT_REQUIRED")
    private String content;

    private Boolean isPublic;

    private Boolean isImportant;

    private NewsStatus status;

    private List<String> employeeIds;
}
