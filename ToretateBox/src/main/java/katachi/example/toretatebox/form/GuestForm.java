package katachi.example.toretatebox.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuestForm {

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String prefecture;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    private String building;
}
