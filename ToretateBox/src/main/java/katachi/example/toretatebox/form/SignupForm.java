package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupForm {

    @NotBlank
    private String name;

    @NotBlank
    private String nameKana;

    @NotBlank
    private String phoneNumber;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    // ▼ 住所系（今は保存しなくてもOK）
    private String postalCode;
    private String prefecture;
    private String city;
    private String address;
    private String building;
}
