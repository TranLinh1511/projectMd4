package ra.model.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.dao.IDAO.IImageDAO;
import ra.model.entity.Image;
import ra.model.service.Iservice.IImageService;

import java.util.List;
@Service
public class ImageServiceImpl implements IImageService {
    @Autowired
    IImageDAO iImageDAO;

    @Override
    public List<Image> getAll() {
        return null;
    }

    @Override
    public int save(Image t) {
        return iImageDAO.save(t);
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Image getById(Integer id) {
        return null;
    }

    @Override
    public List<Image> getByCinemaId(Integer id) {
        return iImageDAO.getByCinemaId(id);
    }

    @Override
    public List<Image> getByTheaterId(Integer id) {
        return iImageDAO.getByTheaterId(id);
    }

    @Override
    public List<Image> getByMovieId(Integer id) {
        return iImageDAO.getByMovieId(id);
    }
}
