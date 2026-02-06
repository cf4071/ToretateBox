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
    private String address; // 都道府県＋市＋番地などを1つにした文字

    private LocalDateTime createdAt;
}
