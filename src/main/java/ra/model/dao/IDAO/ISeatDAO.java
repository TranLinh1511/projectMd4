package ra.model.dao.IDAO;

import ra.model.dao.IGenericDAO;
import ra.model.entity.Seat;

import java.util.List;

public interface ISeatDAO extends IGenericDAO<Seat, Integer> {
    List<Seat> getSeatsByTheaterId(Integer id) ;
}
