
package ra.model.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.request.ReqTheater;
import ra.dto.response.ResCinema;
import ra.dto.response.ResTheater;
import ra.model.dao.IDAO.ICinemaDAO;
import ra.model.dao.IDAO.IImageDAO;
import ra.model.dao.IDAO.ISeatDAO;
import ra.model.dao.IDAO.ITheaterDAO;
import ra.model.entity.Theater;
import ra.model.service.Iservice.ITheaterService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterServiceImpl implements ITheaterService {

    @Autowired
    ITheaterDAO theaterDAO;

    @Autowired
    ISeatDAO seatDAO;

    @Autowired
    IImageDAO imageDAO;

    @Autowired
    ICinemaDAO cinemaDAO;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ResTheater> getAll() {
        List<Theater> theaters = theaterDAO.getAll();
        return getResTheaters(theaters);
    }

    private List<ResTheater> getResTheaters(List<Theater> theaters) {
        return theaters.stream()
                .map(item -> {
                    ResTheater resTheater = modelMapper.map(item, ResTheater.class);
                    resTheater.setSeats(seatDAO.getSeatsByTheaterId(item.getId()));
                    resTheater.setImages(imageDAO.getByTheaterId(item.getId()));
                    resTheater.setCinema(cinemaDAO.getById(item.getCinemaId()));
                    return  resTheater;
                }).collect(Collectors.toList());
    }

    @Override
    public int save(ReqTheater t) {
        return theaterDAO.save(modelMapper.map(t, Theater.class));
    }

    @Override
    public boolean delete(Integer id) {
        return theaterDAO.delete(id);
    }

    @Override
    public ResTheater getById(Integer id) {
        Theater theater = theaterDAO.getById(id);
        ResTheater resTheater = modelMapper.map(theater, ResTheater.class);
        resTheater.setSeats(seatDAO.getSeatsByTheaterId(theater.getId()));
        resTheater.setImages(imageDAO.getByTheaterId(theater.getId()));
        return  resTheater;
    }

    @Override
    public List<ResTheater> getTheaterByCinemaId(Integer id) {
        List<Theater> theaters = theaterDAO.getTheaterByCinemaId(id);
        return getResTheaters(theaters);
    }
}
