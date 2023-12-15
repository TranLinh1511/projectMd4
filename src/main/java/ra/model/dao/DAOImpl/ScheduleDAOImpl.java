package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.IScheduleDAO;
import ra.model.entity.Cinema;
import ra.model.entity.Schedule;
import ra.util.ConnectionDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScheduleDAOImpl implements IScheduleDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<Schedule> getAll() {
        List<Schedule> schedules = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL GetSchedulesData()}");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getInt("id"));
                schedule.setTime(rs.getString("showtime"));
                schedule.setMovieId(rs.getInt("movieId"));
                schedule.setTheaterId(rs.getInt("theaterId"));
                schedule.setCinemaId(rs.getInt("cinemaId"));
                schedule.setShowName(rs.getString("showName"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return schedules;
    }

    @Override
    public int save(Schedule schedule) {
        Connection connection = connectionDatabase.openConnection();
        try {
            if (schedule.getId() == 0) {
                CallableStatement callableStatement = connection.prepareCall("{CALL CreateSchedule(?,?,?,?,?,?)}");
                callableStatement.setString(1, schedule.getTime());
                callableStatement.setInt(2, schedule.getTheaterId());
                callableStatement.setInt(3, schedule.getMovieId());
                callableStatement.setInt(4, schedule.getCinemaId());
                callableStatement.setString(5, schedule.getShowName());
                callableStatement.registerOutParameter(6, Types.INTEGER);
                callableStatement.executeUpdate();
                return callableStatement.getInt(6);
            } else {
                CallableStatement callableStatement = connection.prepareCall("{CALL UpdateSchedule(?,?,?,?,?,?)}");
                callableStatement.setString(1, schedule.getTime());
                callableStatement.setInt(2, schedule.getTheaterId());
                callableStatement.setInt(3, schedule.getMovieId());
                callableStatement.setInt(4, schedule.getCinemaId());
                callableStatement.setInt(5, schedule.getId());
                callableStatement.setString(6, schedule.getShowName());
                callableStatement.executeUpdate();
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
            CallableStatement callableStatement = connection.prepareCall("{CALL DeleteSchedule(?)}");
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
    public Schedule getById(Integer integer) {
        Schedule schedule = new Schedule();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetScheduleById(?)");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                schedule.setId(rs.getInt("id"));
                schedule.setTime(rs.getString("showtime"));
                schedule.setMovieId(rs.getInt("movieId"));
                schedule.setCinemaId(rs.getInt("cinemaId"));
                schedule.setTheaterId(rs.getInt("theaterId"));
                schedule.setShowName(rs.getString("showName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return schedule;
    }
}
