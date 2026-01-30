package katachi.example.toretatebox.domain.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductForm {

    // 編集時に使う（新規登録ではnullでもOK）
    private Integer id;

    @NotBlank(message = "食材名を入力してください")
    private String name;

    private String description;

    @NotNull(message = "価格を入力してください")
    @Min(value = 0, message = "価格は0以上で入力してください")
    private Integer price;

    @NotNull(message = "カテゴリIDを入力してください")
    private Integer categoryId;

    private String season;

    // DBに保存している画像URL（画像を変えない時に残す用）
    private String imageUrl;

    // 画面からアップロードされる画像（DBには入れない）
    private MultipartFile imageFile;
}
