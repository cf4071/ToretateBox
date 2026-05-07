package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditForm {

    @NotBlank(message = "{error.edit.name.required}")
    private String name;

    @NotBlank(message = "{error.edit.nameKana.required}")
    private String nameKana;

    @NotBlank(message = "{error.edit.phone.required}")
    @Pattern(regexp = "^[0-9]+$", message = "{error.edit.phone.pattern}")
    private String phoneNumber;

    @Email(message = "{error.edit.email.format}")
    @NotBlank(message = "{error.edit.email.required}")
    @Size(max = 254, message = "{error.edit.email.size}")
    private String email;

    private String password;

    private String passwordConfirm;

    @NotBlank(message = "{error.edit.postal.required}")
    @Pattern(regexp = "^\\d{7}$|^\\d{3}-\\d{4}$",
             message = "{error.edit.postal.pattern}")
    private String postalCode;

    @NotBlank(message = "{error.edit.prefecture.required}")
    private String prefecture;

    @NotBlank(message = "{error.edit.city.required}")
    @Size(max = 20, message = "市区町村は20文字以内で入力してください")
    private String city;

    @NotBlank(message = "{error.edit.address1.required}")
    @Size(max = 50, message = "{error.edit.address1.size}")
    private String addressLine1;

    @Size(max = 50, message = "{error.edit.address2.size}")
    private String addressLine2;
}