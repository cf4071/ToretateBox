package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupForm {

    @NotBlank(message = "{error.name.required}")
    @Size(max = 20, message = "{error.name.size}")
    private String name;

    @NotBlank(message = "{error.nameKana.required}")
    @Size(max = 20, message = "{error.nameKana.size}")
    private String nameKana;

    @NotBlank(message = "{error.phone.required}")
    @Pattern(regexp = "\\d{10,11}", message = "{error.phone.pattern}")
    private String phoneNumber;

    @Email(message ="{error.email.format}")
    @NotBlank(message = "{error.email.required}")
    @Size(max = 254, message = "{error.email.size}")
    private String email;

    @NotBlank(message = "{error.password.required}")
    @Size(min = 8, message = "{error.password.size}")
    private String password;

    @NotBlank(message = "{error.passwordConfirm.required}")
    private String passwordConfirm;

    @NotBlank(message = "{error.postal.required}")
    @Pattern(regexp = "^\\d{7}$|^\\d{3}-\\d{4}$",
             message = "{error.postal.pattern}")
    private String postalCode;

    @NotBlank(message = "{error.prefecture.required}")
    private String prefecture;

    @NotBlank(message = "{error.city.required}")
    @Size(max = 20, message = "{error.city.size}")
    private String city;

    @NotBlank(message = "{error.address1.required}")
    @Size(max = 50, message = "{error.address1.size}")
    private String addressLine1;

    @Size(max = 50, message = "{error.address2.size}")
    private String addressLine2;
}