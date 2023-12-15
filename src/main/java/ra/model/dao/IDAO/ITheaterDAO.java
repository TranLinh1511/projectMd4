package ra.model.dao.IDAO;

import ra.dto.response.ResTheater;
import ra.model.dao.IGenericDAO;
import ra.model.entity.Theater;

import java.util.List;

public interface ITheaterDAO extends IGenericDAO<Theater, Integer> {
    List<Theater> getTheaterByCinemaId(Integer id);
}
