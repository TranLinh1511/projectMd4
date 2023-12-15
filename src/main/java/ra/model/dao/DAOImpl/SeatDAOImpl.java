package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.ISeatDAO;
import ra.model.entity.Seat;
import ra.util.ConnectionDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeatDAOImpl implements ISeatDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<Seat> getSeatsByTheaterId(Integer id) {
        Connection connection = connectionDatabase.openConnection();
        List<Seat> seats = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetSeatsByTheaterId(?)}");
            callableStatement.setInt(1, id);
            getSeats(seats, callableStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return seats;
    }


    @Override
    public List<Seat> getAll() {
        Connection connection = connectionDatabase.openConnection();
        List<Seat> seats = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetSeatsData()");
            getSeats(seats, callableStatement);
        } catch (SQLException e) {
        }
        return seats;
    }

    @Override
    public int save(Seat seat) {
        Connection connection = connectionDatabase.openConnection();
        int count = 0;
        try {
            if (seat.getId() == 0) {
                CallableStatement callableStatement = connection.prepareCall("{CALL CreateSeat(?,?)}");
                callableStatement.setString(1, seat.getSeatNumber());
                callableStatement.setInt(2, seat.getTheaterId());
                count = callableStatement.executeUpdate();
                if (count > 0) return 1;
            } else {
                CallableStatement callableStatement = connection.prepareCall("{CALL UpdateSeat(?,?,?)}");
                callableStatement.setString(1, seat.getSeatNumber());
                callableStatement.setInt(2, seat.getTheaterId());
                callableStatement.setInt(3, seat.getId());
                count = callableStatement.executeUpdate();
                if (count > 0) return 1;
            }
        } catch (SQLException e) {
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
    public Seat getById(Integer integer) {
        Connection connection = connectionDatabase.openConnection();
        Seat seat = new Seat();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetSeatById(?)}");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                seat.setId(rs.getInt("id"));
                seat.setTheaterId(rs.getInt("theaterId"));
                seat.setSeatNumber(rs.getString("seat_number"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return seat;
    }

    private void getSeats(List<Seat> seats, CallableStatement callableStatement) throws SQLException {
        ResultSet rs = callableStatement.executeQuery();
        while (rs.next()) {
            Seat seat = new Seat();
            seat.setId(rs.getInt("id"));
            seat.setTheaterId(rs.getInt("theaterId"));
            seat.setSeatNumber(rs.getString("seat_number"));
            seats.add(seat);
        }
    }
}
