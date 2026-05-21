package katachi.example.toretatebox.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GuestForm {

    @NotBlank(message = "{error.guest.name.required}")
    @Size(max = 20, message = "{error.guest.name.size}")
    private String name;

    @NotBlank(message = "{error.phone.required}")
    @Pattern(
    	regexp = "^\\d{10,11}$|^\\d{2,4}-\\d{2,4}-\\d{3,4}$",
    	message = "{error.phone.pattern}"
    )
    private String phoneNumber;

    @NotBlank(message = "{error.postal.required}")
    @Pattern(
        regexp = "^\\d{7}$|^\\d{3}-\\d{4}$",
        message = "{error.postal.pattern}"
    )
    private String postalCode;

    @NotBlank(message = "{error.prefecture.required}")
    private String prefecture;

    @NotBlank(message = "{error.city.required}")
    @Size(max = 20, message = "{error.city.size}")
    private String city;

    @NotBlank(message = "{error.address1.required}")
    @Size(max = 50, message = "{error.address1.size}")
    private String addressLine1;

    @Size(max = 50, message = "{error.address2.size}")
    private String addressLine2;
}