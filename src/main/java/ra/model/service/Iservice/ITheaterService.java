package ra.model.service.Iservice;

import ra.dto.request.ReqTheater;
import ra.dto.response.ResProvince;
import ra.dto.response.ResTheater;
import ra.model.entity.Province;
import ra.model.entity.Theater;

import java.util.List;

public interface ITheaterService {
    List<ResTheater> getAll();

    int save(ReqTheater t);

    boolean delete(Integer id);

    ResTheater getById(Integer id);
    List<ResTheater> getTheaterByCinemaId(Integer id);
}
