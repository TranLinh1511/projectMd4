package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.IMovieDAO;
import ra.model.entity.Cinema;
import ra.model.entity.Genre;
import ra.model.entity.Movie;
import ra.util.ConnectionDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieDAOImpl implements IMovieDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<Genre> getGenresByMovieId(Integer id) {
        List<Genre> genres = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetGenresByMovieId(?)");
            callableStatement.setInt(1, id);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getInt("id"));
                genre.setName(rs.getString("name"));
                genres.add(genre);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return genres;
    }

    @Override
    public List<Movie> getMoviesByGenreId(Integer id) {
        List<Movie> movies = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetMoviesByGenreId(?)");
            callableStatement.setInt(1, id);
            getMovie(movies, callableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return movies;
    }

    @Override
    public void bindMovieGenre(Integer movieId, Integer genreId) {
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL BindMovieGenre(?,?)");
            callableStatement.setInt(1, movieId);
            callableStatement.setInt(2, genreId);
            callableStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
    }

    @Override
    public List<Genre> getGenresData() {
        List<Genre> genres = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetGenresData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getInt("id"));
                genre.setName(rs.getString("name"));
                genres.add(genre);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return genres;
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetMoviesData()");
            getMovie(movies, callableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return movies;
    }

    private void getMovie(List<Movie> movies, CallableStatement callableStatement) throws SQLException {
        ResultSet rs = callableStatement.executeQuery();
        while (rs.next()) {
            Movie movie = new Movie();
            getMovies(rs, movie);
            movies.add(movie);
        }
    }

    private void getMovies(ResultSet rs, Movie movie) throws SQLException {
        movie.setId(rs.getInt("id"));
        movie.setName(rs.getString("name"));
        movie.setTitle(rs.getString("title"));
        movie.setDuration(rs.getString("duration"));
        movie.setDirector(rs.getString("director"));
        movie.setDetail(rs.getString("detail"));
        movie.setTrailerUrl(rs.getString("trailerUrl"));
        movie.setReleaseDate(rs.getDate("release_date"));
    }

    @Override
    public int save(Movie movie) {
        Connection connection = connectionDatabase.openConnection();
        try {
            if (movie.getId() == 0) {
                CallableStatement callableStatement = connection.prepareCall("{CALL CreateMovie(?,?,?,?,?,?,?,?)}");
                upMovie(movie, callableStatement);
                callableStatement.registerOutParameter(8, Types.INTEGER);
                callableStatement.executeUpdate();
                return callableStatement.getInt(8);
            } else {
                CallableStatement callableStatement = connection.prepareCall("{CALL UpdateMovie(?,?,?,?,?,?,?,?)}");
                upMovie(movie, callableStatement);
                callableStatement.setInt(8, movie.getId());
                callableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return 0;
    }

    private void upMovie(Movie movie, CallableStatement callableStatement) throws SQLException {
        java.util.Date utilDate = movie.getReleaseDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        callableStatement.setString(1, movie.getName());
        callableStatement.setString(2, movie.getTitle());
        callableStatement.setString(3, movie.getDuration());
        callableStatement.setString(4, movie.getDirector());
        callableStatement.setString(5, movie.getDetail());
        callableStatement.setString(6, movie.getTrailerUrl());
        callableStatement.setDate(7, sqlDate);
    }

    @Override
    public boolean delete(Integer integer) {
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL DeleteMovie(?)}");
            callableStatement.setInt(1, integer);
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return false;
    }

    @Override
    public Movie getById(Integer integer) {
        Movie movie = new Movie();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetMovieById(?)");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                getMovies(rs, movie);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return movie;
    }


}
