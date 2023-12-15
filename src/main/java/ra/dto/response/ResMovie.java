package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.entity.Genre;
import ra.model.entity.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResMovie {
    private int id;
    private String name, title, director, trailerUrl, detail,duration;
    private Date releaseDate;
    private List<Image> images = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
}
