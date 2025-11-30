package katachi.example.toretatebox.domain.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserView {
    private Integer id;
    private String name;
    private String email;
    private boolean is_admin;
    private LocalDateTime created_at;
}
