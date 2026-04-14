package katachi.example.toretatebox.domain.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductForm {

    private Integer id;

    @NotBlank(message = "食材名を入力してください")
    @Size(max = 50, message = "食材名は50文字以内で入力してください")
    private String name;

    @NotNull(message = "カテゴリを選んでください")
    private Integer categoryId;

    @NotBlank(message = "旬の季節を選んでください")
    @Size(max = 20, message = "旬の季節は20文字以内で入力してください")
    private String season;

    @Size(max = 500, message = "食材説明は500文字以内で入力してください")
    private String description;

    @NotNull(message = "値段を入力してください")
    @Min(value = 1, message = "値段は1円以上で入力してください")
    private Integer price;

    private String imageUrl;

    private MultipartFile imageFile;
}