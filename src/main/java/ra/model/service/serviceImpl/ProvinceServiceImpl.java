package ra.model.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.response.ResProvince;
import ra.model.dao.IDAO.ICinemaDAO;
import ra.model.dao.IDAO.IProvinceDAO;
import ra.model.entity.Province;
import ra.model.service.Iservice.IProvinceService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl implements IProvinceService {
    @Autowired
    IProvinceDAO iProvinceDAO;

    @Autowired
    ICinemaDAO iCinemaDAO;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ResProvince> getAll() {
        List<Province> provinces = iProvinceDAO.getAll();
        return provinces.stream()
                .map(item -> {
                    ResProvince resProvince = modelMapper.map(item, ResProvince.class);
                    resProvince.setCinemas(iCinemaDAO.findByProvinceId(item.getId()));
                    return resProvince;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean save(Province t) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public ResProvince getById(Integer id) {
        ResProvince resProvince = modelMapper.map(iProvinceDAO.getById(id), ResProvince.class);
        resProvince.setCinemas(iCinemaDAO.findByProvinceId(resProvince.getId()));
        return resProvince;
    }
}
