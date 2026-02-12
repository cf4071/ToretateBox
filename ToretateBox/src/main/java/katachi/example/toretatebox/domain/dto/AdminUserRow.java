package katachi.example.toretatebox.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserRow {
    private int id;
    private String email;
    private String name;
    private String phoneNumber;

    private String postalCode;
    private String address; 

    private LocalDateTime createdAt;
}
