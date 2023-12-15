package ra.model.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.dto.request.ReqCinema;
import ra.dto.response.ResCinema;
import ra.dto.response.ResProvince;
import ra.model.dao.IDAO.ICinemaDAO;
import ra.model.dao.IDAO.IImageDAO;
import ra.model.dao.IDAO.IProvinceDAO;
import ra.model.entity.Cinema;
import ra.model.entity.Image;
import ra.model.entity.Province;
import ra.model.service.Iservice.ICinemaService;
import ra.model.service.Iservice.IProvinceService;
import ra.util.ConnectionDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CinemaServiceImpl implements ICinemaService {
    @Autowired
    ICinemaDAO iCinemaDAO;

    @Autowired
    IImageDAO iImageDAO;
    @Autowired
    IProvinceService iProvinceService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ResCinema> getAll() {
        List<Cinema> cinemas = iCinemaDAO.getAll();
        return cinemas.stream()
                .map(item -> {
                    ResCinema resCinema = modelMapper.map(item, ResCinema.class);
                    resCinema.setImages(iImageDAO.getByCinemaId(item.getId()));
                    resCinema.setProvince(iProvinceService.getById(item.getProvinceId()));
                    return resCinema;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int save(ReqCinema t) {
        return iCinemaDAO.save(modelMapper.map(t, Cinema.class));
    }

    @Override
    public boolean delete(Integer id) {
        return iCinemaDAO.delete(id);
    }

    @Override
    public ResCinema getById(Integer id) {
        return modelMapper.map(iCinemaDAO.getById(id), ResCinema.class);
    }

    @Override
    public List<ResCinema> getByProvinceId(Integer id) {
        List<Cinema> cinemas = iCinemaDAO.findByProvinceId(id);
        return cinemas.stream()
                .map(item -> {
                    ResCinema resCinema = modelMapper.map(item, ResCinema.class);
                    resCinema.setImages(iImageDAO.getByCinemaId(item.getId()));
                    resCinema.setProvince(iProvinceService.getById(item.getProvinceId()));
                    return resCinema;
                })
                .collect(Collectors.toList());
    }
}
