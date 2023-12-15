package ra.util;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Repository
public class ConnectionDatabase {
    private final String DRIVER_JDBC = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/cgv";
    private final String USERNAME = "root";
    private final String PASSWORD = "Aa151120";

    public  Connection openConnection() {
       Connection connection = null;
        try {
            Class.forName(DRIVER_JDBC);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi 1");
        } catch (SQLException e) {
        }
        return  connection;
    }

    public void  closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Lỗi 2");
        }
    }
}
