package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResTicketDetail {
    private String userName, movieName, time, cinemaName, theaterName, seatNumber;
}
