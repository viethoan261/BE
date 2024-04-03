package hoan.com.springboot.common.error;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FieldErrorResponse implements Serializable {
    private String field;

    private String objectName;

    private String message;
}
