package ra.model.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.dao.IDAO.ISeatDAO;
import ra.model.entity.Seat;
import ra.model.service.Iservice.ISeatService;

import java.util.List;
@Service
public class SeatServiceImpl implements ISeatService {
    @Autowired
    ISeatDAO seatDAO;

    @Override
    public List<Seat> getAll() {
        return seatDAO.getAll();
    }

    @Override
    public int save(Seat t) {
        return seatDAO.save(t);
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Seat getById(Integer id) {
        return seatDAO.getById(id);
    }

    @Override
    public List<Seat> getSeatsByTheaterId(Integer id) {
        return seatDAO.getSeatsByTheaterId(id);
    }
}
