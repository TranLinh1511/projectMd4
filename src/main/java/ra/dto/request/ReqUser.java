package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReqUser {
    private int id;
    @NotEmpty(message = "Họ tên không được để trống!!")
    private String fullName;
    @Email(message = "Email không đúng định dạng!!")
    @NotEmpty(message = "Email không được để trống!!")
    private String email;
    @Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại không đúng định dạng")
    @NotEmpty(message = "Số điện thoại không được để trống!!")
    private String phone;

    @NotEmpty(message = "Mật khẩu không được để trống!!")
    private String password;

    @NotNull(message = "Tuổi không được để trống!!")
    private int age;
    private boolean gender;
    private boolean role = false;
}
