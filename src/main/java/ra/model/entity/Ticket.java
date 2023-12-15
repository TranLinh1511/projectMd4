package ra.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Ticket {
    private int id, scheduleId,userId,seatId;
    private double price;
    private boolean isCancelled = false;
}
