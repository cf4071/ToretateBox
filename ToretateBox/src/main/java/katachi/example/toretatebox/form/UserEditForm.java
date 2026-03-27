package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditForm {

    @NotBlank(message = "氏名を入力してください")
    private String name;

    @NotBlank(message = "フリガナを入力してください")
    private String nameKana;

    @NotBlank(message = "電話番号を入力してください")
    private String phoneNumber;

    @Email
    @NotBlank(message = "メールアドレスを入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    private String password;

    @NotBlank(message = "パスワード(再確認)を入力してください")
    private String passwordConfirm;

    @NotBlank(message = "郵便番号を入力してください")
    private String postalCode;

    @NotBlank(message = "都道府県を入力してください")
    private String prefecture;

    @NotBlank(message = "市区町村を入力してください")
    private String city;

    @NotBlank(message = "町名・番地を入力してください")
    private String addressLine1;
    
    @Size(max=50, message = "建物名・部屋番号は50文字以内で入力してください")
    private String addressLine2;
}
