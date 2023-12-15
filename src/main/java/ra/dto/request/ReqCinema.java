package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Province;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReqCinema {
    private int id,provinceId;

    @NotEmpty(message = "Tên rạp không được để trống!!")
    private String name;

    @NotEmpty(message = "Địa điểm không được để trống!!")
    private String location;

    @NotEmpty(message = "Số điện thoại không được để trống!!")
    private String phone;



}
