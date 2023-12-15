package ra.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    private int id, age;
    private String fullName,email,phone, password;
    private boolean gender, role = false;
    private boolean status;
}
