package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResSchedule {
    private int id;
    private String time, showName;
    private ResTheater theater;
    private ResMovie movie;
    private ResCinema cinema;
    private List<ResTicket> tickets = new ArrayList<>();
}
