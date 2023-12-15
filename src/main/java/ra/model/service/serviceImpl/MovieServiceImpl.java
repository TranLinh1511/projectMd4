package ra.model.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.request.ReqMovie;
import ra.dto.response.ResMovie;
import ra.model.dao.IDAO.IImageDAO;
import ra.model.dao.IDAO.IMovieDAO;
import ra.model.entity.Genre;
import ra.model.entity.Movie;
import ra.model.service.Iservice.IMovieService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements IMovieService {
    @Autowired
    IMovieDAO movieDAO;
    @Autowired
    IImageDAO imageDAO;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ResMovie> getAll() {
        List<Movie> movies = movieDAO.getAll();
        return convertResMovie(movies);
    }

    @Override
    public int save(ReqMovie t) {
        return movieDAO.save(modelMapper.map(t, Movie.class));
    }

    @Override
    public boolean delete(Integer id) {
        return movieDAO.delete(id);
    }

    @Override
    public ResMovie getById(Integer id) {
        Movie movie = movieDAO.getById(id);
        ResMovie resMovie = modelMapper.map(movie, ResMovie.class);
        resMovie.setImages(imageDAO.getByMovieId(movie.getId()));
        resMovie.setGenres(movieDAO.getGenresByMovieId(movie.getId()));
        return resMovie;
    }

    @Override
    public List<Genre> getGenresByMovieId(Integer id) {
        return movieDAO.getGenresByMovieId(id);
    }

    @Override
    public List<ResMovie> getMoviesByGenreId(Integer id) {
        List<Movie> movies = movieDAO.getMoviesByGenreId(id);
        return convertResMovie(movies);
    }

    @Override
    public void bindMovieGenre(Integer movieId, Integer genreId) {
        movieDAO.bindMovieGenre(movieId, genreId);
    }

    @Override
    public List<Genre> getGenresData() {
        return movieDAO.getGenresData();
    }

    private List<ResMovie> convertResMovie(List<Movie> movies) {
        return movies.stream().map(item -> {
            ResMovie resMovie = modelMapper.map(item, ResMovie.class);
            resMovie.setImages(imageDAO.getByMovieId(item.getId()));
            resMovie.setGenres(movieDAO.getGenresByMovieId(item.getId()));
            return resMovie;
        }).collect(Collectors.toList());
    }
}
