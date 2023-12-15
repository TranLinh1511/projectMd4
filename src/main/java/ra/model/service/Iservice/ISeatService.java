package ra.model.service.Iservice;

import ra.model.entity.Seat;

import java.util.List;

public interface ISeatService {
    List<Seat> getAll();
    int save(Seat t);
    boolean delete(Integer id);
    Seat getById(Integer id);
    List<Seat> getSeatsByTheaterId(Integer id) ;

}
