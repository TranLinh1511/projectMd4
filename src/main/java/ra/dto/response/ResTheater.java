package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Cinema;
import ra.model.entity.Image;
import ra.model.entity.Seat;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResTheater {
    private int id;
    private String name;
    private List<Image> images = new ArrayList<>();
    private List<Seat> seats = new ArrayList<>();
    private Cinema cinema;
}
