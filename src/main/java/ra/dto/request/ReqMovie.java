package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ra.model.entity.Genre;
import ra.model.entity.Image;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReqMovie {
    private int id;

    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotEmpty(message = "Tiêu đề không được để trống")
    private String title;

    @NotEmpty(message = "Đạo diễn không được để trống")
    private String director;

    private String trailerUrl;
    private String detail;
    private String duration;

    @NotNull(message = "Ngày phát hành không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    @NotNull(message = "Yêu cầu ít nhất 1 thể loại")
    private List<Integer> genresId = new ArrayList<>();
}
