package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEditForm {

    // ▼ usersテーブル
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

    // ▼ addressesテーブル（GuestFormと同じ）
    @NotBlank
    private String postalCode;

    @NotBlank
    private String prefecture;

    @NotBlank
    private String city;

    @NotBlank
    private String addressLine1;

    private String addressLine2;
}
