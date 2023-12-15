package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.IImageDAO;
import ra.model.entity.Image;
import ra.util.ConnectionDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageDAOImpl implements IImageDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<Image> getAll() {
        Connection connection = connectionDatabase.openConnection();
        List<Image> images = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetImageData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setImageName(rs.getString("imageName"));
                image.setCinemaId(rs.getInt("cinemaId"));
                image.setMovieId(rs.getInt("movieId"));
                image.setTheaterId(rs.getInt("TheaterId"));
                images.add(image);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return images;
    }

    @Override
    public int save(Image image) {
        Connection connection = connectionDatabase.openConnection();
        int count;
        try {
            if (image.getId() == 0) {
                CallableStatement callableStatement;
                if (image.getCinemaId() != 0) {
                    callableStatement = connection.prepareCall("{CALL CreateImage(?,?,null,null)}");
                    callableStatement.setString(1, image.getImageName());
                    callableStatement.setInt(2, image.getCinemaId());
                } else if (image.getMovieId() != 0) {
                    callableStatement = connection.prepareCall("{CALL CreateImage(?,null,null,?)}");
                    callableStatement.setString(1, image.getImageName());
                    callableStatement.setInt(2, image.getMovieId());
                } else {
                    callableStatement = connection.prepareCall("{CALL CreateImage(?,null,?,null)}");
                    callableStatement.setString(1, image.getImageName());
                    callableStatement.setInt(2, image.getTheaterId());
                }
                count = callableStatement.executeUpdate();
            } else {
                CallableStatement callableStatement;
                if (image.getCinemaId() != 0) {
                    callableStatement = connection.prepareCall("{CALL UpdateImage(?,?,null,null,?)}");
                    callableStatement.setString(1, image.getImageName());
                    callableStatement.setInt(2, image.getCinemaId());
                    callableStatement.setInt(3, image.getId());
                } else if (image.getMovieId() != 0) {
                    callableStatement = connection.prepareCall("{CALL UpdateImage(?,null,null,?,?)}");
                    callableStatement.setString(1, image.getImageName());
                    callableStatement.setInt(2, image.getMovieId());
                    callableStatement.setInt(3, image.getId());
                } else {
                    callableStatement = connection.prepareCall("{CALL UpdateImage(?,null,?,null,?)}");
                    callableStatement.setString(1, image.getImageName());
                    callableStatement.setInt(2, image.getTheaterId());
                    callableStatement.setInt(3, image.getId());
                }
                count = callableStatement.executeUpdate();
            }
            if (count > 0) return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return 0;
    }


    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public Image getById(Integer integer) {
        return null;
    }

    @Override
    public List<Image> getByCinemaId(Integer id) {
        return getImagesByParameter("GetImagesByCinemaId", id);
    }

    @Override
    public List<Image> getByTheaterId(Integer id) {
        return getImagesByParameter("GetImagesByTheaterId", id);
    }

    @Override
    public List<Image> getByMovieId(Integer id) {
        return getImagesByParameter("GetImagesByMovieId", id);
    }

    public List<Image> getImagesByParameter(String storedProcedure, Integer id) {
        Connection connection = connectionDatabase.openConnection();
        List<Image> images = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL " + storedProcedure + "(?)}");
            callableStatement.setInt(1, id);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setImageName(rs.getString("imageName"));
                images.add(image);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return images;
    }

}
