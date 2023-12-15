package ra.model.dao.DAOImpl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.IUserDAO;
import ra.model.entity.User;
import ra.util.ConnectionDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements IUserDAO {

    @Autowired
    ConnectionDatabase connectionDatabase;

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetUserData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setAge(rs.getInt("age"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getBoolean("gender"));
                user.setRole(rs.getBoolean("role"));
                user.setStatus(rs.getBoolean("status"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi get");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return list;
    }

    @Override
    public int save(User user) {
        Connection connection = connectionDatabase.openConnection();
        try {
            int count;
            if (user.getId() == 0) {
                CallableStatement callableStatement = connection.prepareCall("{CALL CreateUser(?,?,?,?,?,?,?)}");
                saveSql(user, callableStatement);
                count = callableStatement.executeUpdate();
            } else {
                CallableStatement callableStatement = connection.prepareCall("{CALL UpdateUser(?,?,?,?,?,?,?,?)}");
                saveSql(user, callableStatement);
                callableStatement.setInt(8, user.getId());
                callableStatement.executeUpdate();
            }
            return user.getId();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return 0;
    }

    private void saveSql(User user, CallableStatement callableStatement) throws SQLException {
        callableStatement.setString(1, user.getFullName());
        callableStatement.setString(2, user.getEmail());
        callableStatement.setInt(3, user.getAge());
        callableStatement.setBoolean(4, user.isGender());
        callableStatement.setString(5, user.getPhone());
        callableStatement.setString(6, user.getPassword());
        callableStatement.setBoolean(7, user.isRole());
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public User getById(Integer integer) {
        return null;
    }


    @Override
    public boolean blockUser(Integer userId) {
        Connection connection = connectionDatabase.openConnection();
        int count = 0;
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL BlockUser(?)");
            callableStatement.setInt(1, userId);
            count = callableStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi get");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unBlockUser(Integer userId) {
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL UnblockUser(?)");
            callableStatement.setInt(1, userId);
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi get");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
    }

    @Override
    public User userLogin(String email, String password) {

        User user = getUserByEmail(email);
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    private User getUserByEmail(String email) {
        User user = new User();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL CheckLogin(?)");
            callableStatement.setString(1, email);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setAge(rs.getInt("age"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getBoolean("gender"));
                user.setRole(rs.getBoolean("role"));
                user.setStatus(rs.getBoolean("status"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return user;
    }


}
