package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditForm {

    @NotBlank(message = "氏名を入力してください")
    private String name;

    @NotBlank(message = "フリガナを入力してください")
    private String nameKana;

    @NotBlank(message = "電話番号を入力してください")
    @Pattern(regexp = "^[0-9]+$", message = "電話番号は数字で入力してください")
    private String phoneNumber;

    @Email(message = "正しいメールアドレス形式で入力してください")
    @NotBlank(message = "メールアドレスを入力してください")
    @Size(max = 254, message = "メールアドレスは254文字以内で入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    private String password;

    @NotBlank(message = "パスワード（再確認）を入力してください")
    private String passwordConfirm;

    @NotBlank(message = "郵便番号を入力してください")
    @Pattern(regexp = "^\\d{7}$|^\\d{3}-\\d{4}$",
             message = "郵便番号は7桁またはハイフン付きで入力してください")
    private String postalCode;

    @NotBlank(message = "都道府県を入力してください")
    private String prefecture;

    @NotBlank(message = "市区町村を入力してください")
    @Size(max = 20, message = "市区町村は20文字以内で入力してください")
    private String city;

    @NotBlank(message = "町名・番地を入力してください")
    @Size(max = 50, message = "町名・番地は50文字以内で入力してください")
    private String addressLine1;

    @Size(max = 50, message = "建物名・部屋番号は50文字以内で入力してください")
    private String addressLine2;
}