package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank(message = "{error.login.email.required}")
    @Email(message = "{error.login.email.format}")
    private String email;

    @NotBlank(message = "{error.login.password.required}")
    private String password;
}
