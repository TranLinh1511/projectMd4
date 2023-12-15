package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.ICinemaDAO;
import ra.model.entity.Cinema;
import ra.util.ConnectionDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class CinemaDAOImpl implements ICinemaDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<Cinema> getAll() {
        List<Cinema> cinemas = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            PreparedStatement callableStatement = connection.prepareStatement("CALL GetCinemaData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Cinema cinema = new Cinema();
                cinema.setId(rs.getInt("id"));
                cinema.setName(rs.getString("name"));
                cinema.setLocation(rs.getString("location"));
                cinema.setPhone(rs.getString("phone"));
                cinema.setProvinceId(rs.getInt("provinceId"));
                cinemas.add(cinema);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy rạp");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return cinemas;
    }

    @Override
    public int save(Cinema cinema) {
        Connection connection = connectionDatabase.openConnection();
        try {
            if (cinema.getId() == 0) {
                CallableStatement callableStatement = connection.prepareCall("{CALL CreateCinema(?,?,?,?,?)}");
                callableStatement.setString(1, cinema.getName());
                callableStatement.setString(2, cinema.getLocation());
                callableStatement.setInt(3, cinema.getProvinceId());
                callableStatement.setString(4, cinema.getPhone());
                callableStatement.registerOutParameter(5, Types.INTEGER);
                callableStatement.executeUpdate();
                return callableStatement.getInt(5);
            } else {
                CallableStatement callableStatement = connection.prepareCall("{CALL UpdateCinema(?,?,?,?,?)}");
                callableStatement.setString(1, cinema.getName());
                callableStatement.setString(2, cinema.getLocation());
                callableStatement.setInt(3, cinema.getProvinceId());
                callableStatement.setString(4, cinema.getPhone());
                callableStatement.setInt(5, cinema.getId());
                callableStatement.executeUpdate();
            }
            return cinema.getId();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return 0;
    }


    @Override
    public boolean delete(Integer integer) {
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL DeleteCinema(?)}");
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
    public Cinema getById(Integer integer) {
        Cinema cinema = new Cinema();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetCinemaById(?)");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                cinema.setId(rs.getInt("id"));
                cinema.setName(rs.getString("name"));
                cinema.setLocation(rs.getString("location"));
                cinema.setProvinceId(rs.getInt("provinceId"));
                cinema.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi get");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return cinema;
    }

    @Override
    public List<Cinema> findByProvinceId(Integer integer) {
        List<Cinema> cinemas = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetCinemaByProvinceId(?)");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Cinema cinema = new Cinema();
                cinema.setId(rs.getInt("id"));
                cinema.setName(rs.getString("name"));
                cinema.setLocation(rs.getString("location"));
                cinema.setPhone(rs.getString("phone"));
                cinemas.add(cinema);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi ở đây");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return cinemas;
    }

}
