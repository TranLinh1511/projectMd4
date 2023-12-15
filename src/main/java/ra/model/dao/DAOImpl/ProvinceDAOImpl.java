package ra.model.dao.DAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.model.dao.IDAO.IProvinceDAO;
import ra.model.entity.Province;
import ra.util.ConnectionDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProvinceDAOImpl implements IProvinceDAO {
    @Autowired
    ConnectionDatabase connectionDatabase;
    @Autowired
    CinemaDAOImpl cinemaDAO;
    @Override
    public List<Province> getAll() {
        List<Province> provinces = new ArrayList<>();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetProvinceData()");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Province province = new Province();
                province.setId(rs.getInt("id"));
                province.setName(rs.getString("name"));
                provinces.add(province);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi get");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return provinces;
    }

    @Override
    public int save(Province province) {
        return 0;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public Province getById(Integer integer) {
        Province province = new Province();
        Connection connection = connectionDatabase.openConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("CALL GetProvinceById(?)");
            callableStatement.setInt(1, integer);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                province.setId(rs.getInt("id"));
                province.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi get");
        } finally {
            connectionDatabase.closeConnection(connection);
        }
        return province;
    }
}
