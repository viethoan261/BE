package hoan.com.springboot.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import hoan.com.springboot.common.util.Response;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represent http response body
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> extends Response<T> {
    private String error;

    @Builder
    public ErrorResponse(int code, String message, T data, String error) {
        this.setData(data);
        this.setCode(code);
        this.setMessage(message);
        this.setSuccess(false);
        this.error = error;
    }
}
