package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReqSchedule {
    private int id;
    @NotEmpty(message = "Thời gian sự kiện không được để trống!!")
    private String showTime;
    private String showName;
    private int theaterId;
    private int movieId;
    private int cinemaId;
    private double price;
}
