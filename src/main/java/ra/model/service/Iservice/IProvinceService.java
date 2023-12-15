package ra.model.service.Iservice;
import ra.dto.response.ResProvince;
import ra.model.entity.Province;

import java.util.List;

public interface IProvinceService {
    List<ResProvince> getAll();

    boolean save(Province t);

    boolean delete(Integer id);

    ResProvince getById(Integer id);
}
