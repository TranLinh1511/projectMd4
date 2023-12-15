package ra.model.service.Iservice;

import ra.dto.request.ReqMovie;
import ra.dto.response.ResMovie;
import ra.model.entity.Genre;
import java.util.List;

public interface IMovieService {
    List<ResMovie> getAll();
    int save(ReqMovie t);
    boolean delete(Integer id);
    ResMovie getById(Integer id);
    List<Genre> getGenresByMovieId(Integer id);
    List<ResMovie> getMoviesByGenreId(Integer id);
    void bindMovieGenre(Integer movieId, Integer genreId );
    List<Genre> getGenresData();
}
