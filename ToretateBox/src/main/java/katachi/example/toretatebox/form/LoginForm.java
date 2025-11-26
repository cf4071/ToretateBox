package katachi.example.toretatebox.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "正しい形式で入力してください。")
    private String email;

    @NotBlank(message = "パスワードを入力してください。")
    private String password;
}
