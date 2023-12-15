package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.dto.response.ResTicketDetail;
import ra.model.dao.IDAO.ITicketDAO;
import ra.model.entity.Schedule;
import ra.model.entity.Ticket;
import ra.model.entity.User;
import ra.util.ConnectionDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketDAOImpl implements ITicketDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<Ticket> getAll() {
        List<Ticket> tickets = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetTicketsData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSeatId(rs.getInt("seat_id"));
                ticket.setScheduleId(rs.getInt("scheduleId"));
                ticket.setPrice(rs.getDouble("price"));
                ticket.setCancelled(rs.getBoolean("is_cancelled"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch trình");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return tickets;
    }

    @Override
    public int save(Ticket ticket) {
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL CreateTicket(?,?,?)}");
            callableStatement.setInt(1, ticket.getSeatId());
            callableStatement.setInt(2, ticket.getScheduleId());
            callableStatement.setDouble(3, ticket.getPrice());
            callableStatement.executeUpdate();
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
    public Ticket getById(Integer integer) {
        Ticket ticket = new Ticket();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetTicketsData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                ticket.setId(rs.getInt("id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setScheduleId(rs.getInt("scheduleId"));
                ticket.setPrice(rs.getDouble("price"));
                ticket.setCancelled(rs.getBoolean("is_cancelled"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch trình");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return ticket;
    }

    @Override
    public List<Ticket> getTicketsByScheduleId(Integer id) {
        List<Ticket> tickets = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetTicketsByScheduleId(?)");
            callableStatement.setInt(1, id);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSeatId(rs.getInt("seat_id"));
                ticket.setScheduleId(rs.getInt("scheduleId"));
                ticket.setPrice(rs.getDouble("price"));
                ticket.setCancelled(rs.getBoolean("is_cancelled"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return tickets;
    }

    @Override
    public void bindingUser(Integer userId, Integer ticketId) {
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{CALL BindingUser(?,?)}");
            callableStatement.setInt(1, userId);
            callableStatement.setInt(2, ticketId);
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
    }

    @Override
    public List<ResTicketDetail> getTicketByUserId(Integer id) {
        List<ResTicketDetail> ticketDetails = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetTicketByUserId(?)");
            callableStatement.setInt(1, id);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                ResTicketDetail ticket = new ResTicketDetail();
                ticket.setUserName(rs.getString("userName"));
                ticket.setMovieName(rs.getString("movieName"));
                ticket.setTime(rs.getString("time"));
                ticket.setCinemaName(rs.getString("cinemaName"));
                ticket.setTheaterName(rs.getString("theaterName"));
                ticket.setSeatNumber(rs.getString("seatNumber"));
                ticketDetails.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return ticketDetails;
    }
}
