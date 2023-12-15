package ra.model.dao.IDAO;

import ra.model.dao.IGenericDAO;
import ra.model.entity.Cinema;

import java.util.List;

public interface ICinemaDAO extends IGenericDAO<Cinema, Integer> {
    List<Cinema> findByProvinceId(Integer id);
}
