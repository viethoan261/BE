package hoan.com.springboot.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UpdateUserRequest {

    private Set<Integer> roles;
    private int id;
    @NotBlank
    @Size(min = 6, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    @Email
    private String email;

    private boolean status;

    private boolean delete;

    private String mobile;

    private Boolean gender;

    private String code;

    private String fullname;

    private String address;
}
