package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.dto.response.ResTheater;
import ra.model.dao.IDAO.ITheaterDAO;
import ra.model.entity.Image;
import ra.model.entity.Theater;
import ra.util.ConnectionDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class TheaterDAOImpl implements ITheaterDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;


    @Override
    public List<Theater> getAll() {
        Connection connection = connectionDatabase.openConnection();
        List<Theater> theaters = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetTheaterData()}");
            getTheater(theaters, callableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return theaters;
}

    @Override
    public int save(Theater theater) {
        Connection connection = connectionDatabase.openConnection();
        int count;
        try {
            if (theater.getId() == 0) {
                CallableStatement callableStatement = connection.prepareCall("{CALL CreateTheater(?,?,?,?)}");
                callableStatement.setString(1, theater.getName());
                callableStatement.setInt(2, theater.getTotalSeat());
                callableStatement.setInt(3, theater.getCinemaId());
                callableStatement.registerOutParameter(4, Types.INTEGER);
                callableStatement.executeUpdate();
                return callableStatement.getInt(4);
            } else {
                CallableStatement callableStatement = connection.prepareCall("{CALL UpdateTheater(?,?,?,?)}");
                callableStatement.setString(1, theater.getName());
                callableStatement.setInt(2, theater.getTotalSeat());
                callableStatement.setInt(3, theater.getCinemaId());
                callableStatement.setInt(4, theater.getId());
                count = callableStatement.executeUpdate();
                if (count > 0) return 1;
            }
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
            CallableStatement callableStatement = connection.prepareCall("{CALL DeleteTheater(?)}");
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
    public Theater getById(Integer integer) {
        Connection connection = connectionDatabase.openConnection();
        Theater theater = new Theater();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetTheaterById(?)}");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                theater.setId(rs.getInt("id"));
                theater.setName(rs.getString("name"));
                theater.setCinemaId(rs.getInt("cinema_id"));
                theater.setTotalSeat(rs.getInt("total_seats"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return theater;
    }

    @Override
    public List<Theater> getTheaterByCinemaId(Integer id) {
        Connection connection = connectionDatabase.openConnection();
        List<Theater> theaters = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetTheaterByCinemaId(?)}");
            callableStatement.setInt(1, id);
            getTheater(theaters, callableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return theaters;
    }

    private void getTheater(List<Theater> theaters, CallableStatement callableStatement) throws SQLException {
        ResultSet rs = callableStatement.executeQuery();
        while (rs.next()) {
            Theater theater = new Theater();
            theater.setId(rs.getInt("id"));
            theater.setName(rs.getString("name"));
            theater.setTotalSeat(rs.getInt("total_seats"));
            theater.setCinemaId(rs.getInt("cinema_id"));
            theaters.add(theater);
        }
    }
}
