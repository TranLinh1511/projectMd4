package ra.model.dao.IDAO;

import ra.dto.response.ResGenre;
import ra.dto.response.ResMovie;
import ra.model.dao.IGenericDAO;
import ra.model.entity.Genre;
import ra.model.entity.Movie;

import java.util.List;

public interface IMovieDAO extends IGenericDAO<Movie, Integer> {
    List<Genre> getGenresByMovieId(Integer id);
    List<Movie> getMoviesByGenreId(Integer id);

    void bindMovieGenre(Integer movieId, Integer genreId );
    List<Genre> getGenresData();
}
