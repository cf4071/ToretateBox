package katachi.example.toretatebox.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GuestForm {

    @NotBlank(message = "宛名を入力してください")
    @Size(max = 20, message = "宛名は20文字以内で入力してください")
    private String name;

    @NotBlank(message = "電話番号を入力してください")
    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phoneNumber;

    @NotBlank(message = "郵便番号を入力してください")
    private String postalCode;

    @NotBlank(message = "都道府県を入力してください")
    @Size(max = 10, message = "都道府県は10文字以内で入力してください")
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