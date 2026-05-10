package katachi.example.toretatebox.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductForm {

    private Integer id;

    @NotBlank(message = "{error.product.name.required}")
    @Size(max = 50, message = "{error.product.name.size}")
    private String name;

    @NotNull(message = "{error.product.category.required}")
    private Integer categoryId;

    @NotBlank(message = "{error.product.season.required}")
    @Size(max = 20, message = "{error.product.season.size}")
    private String season;

    @Size(max = 500, message = "{error.product.description.size}")
    private String description;

    @NotNull(message = "{error.product.price.required}")
    @Min(value = 1, message = "{error.product.price.mi}")
    private Integer price;

    private String imageUrl;

    private MultipartFile imageFile;
}