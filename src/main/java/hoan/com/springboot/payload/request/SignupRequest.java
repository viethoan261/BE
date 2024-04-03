package hoan.com.springboot.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<Long> roles;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String mobile;

    private Boolean gender;

    private String code;

    private String fullname;

    private String address;


}
