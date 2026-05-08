package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditForm {

    @NotBlank(message = "{error.name.required}")
    private String name;

    @NotBlank(message = "{error.nameKana.required}")
    private String nameKana;

    @NotBlank(message = "{error.phone.required}")
    @Pattern(regexp = "^[0-9]+$", message = "{error.phone.pattern}")
    private String phoneNumber;

    @Email(message = "{error.email.format}")
    @NotBlank(message = "{error.email.required}")
    @Size(max = 254, message = "{error.email.size}")
    private String email;

    private String password;

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