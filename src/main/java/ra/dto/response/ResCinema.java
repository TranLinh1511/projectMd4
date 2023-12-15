package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Image;
import ra.model.entity.Province;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResCinema {
    private int id;
    private String name, location, phone;
    private List<Image> images = new ArrayList<>();
    private ResProvince province;
}
