package ra.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Timer;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Movie {
    private int id;
    private String name, title, director, trailerUrl, detail,duration;
    private Date releaseDate;
}
