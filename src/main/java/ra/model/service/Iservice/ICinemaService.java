package ra.model.service.Iservice;

import ra.dto.request.ReqCinema;
import ra.dto.response.ResCinema;
import ra.model.entity.Cinema;
import ra.model.entity.Province;

import java.util.List;

public interface ICinemaService {
    List<ResCinema> getAll();

    int save(ReqCinema t);

    boolean delete(Integer id);

    ResCinema getById(Integer id);

    List<ResCinema> getByProvinceId(Integer id);
}
