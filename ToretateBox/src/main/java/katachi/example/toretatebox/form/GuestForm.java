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

    @NotBlank(message = "{error.guest.phone.required}")
    @Size(max = 20, message = "{error.guest.phone.size}")
    @Pattern(regexp = "^[0-9]+$", message = "{error.guest.phone.pattern}")
    private String phoneNumber;

    @NotBlank(message = "{error.guest.postal.required}")
    @Pattern(
        regexp = "^\\d{7}$|^\\d{3}-\\d{4}$",
        message = "{error.guest.postal.pattern}"
    )
    private String postalCode;

    @NotBlank(message = "{error.guest.prefecture.required}")
    private String prefecture;

    @NotBlank(message = "{error.guest.city.required}")
    @Size(max = 20, message = "{error.guest.city.size}")
    private String city;

    @NotBlank(message = "{error.guest.address1.required}")
    @Size(max = 50, message = "{error.guest.address1.size}")
    private String addressLine1;

    @Size(max = 50, message = "{error.guest.address2.size}")
    private String addressLine2;
}