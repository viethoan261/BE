package hoan.com.springboot.payload.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequest extends Request{

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
