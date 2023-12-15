package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReqTheater {
    private int id,cinemaId;

    @NotEmpty(message = "Tên rạp không được để trống!!")
    private String name;

    @NotNull(message = "Tổng số ghế không được để trống")
    private Integer totalSeat;
}
