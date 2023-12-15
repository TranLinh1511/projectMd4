package ra.model.dao.IDAO;

import ra.model.dao.IGenericDAO;
import ra.model.entity.Cinema;
import ra.model.entity.Image;

import java.util.List;

public interface IImageDAO extends IGenericDAO<Image, Integer> {
    List<Image> getByCinemaId(Integer id);
    List<Image> getByTheaterId(Integer id);
    List<Image> getByMovieId(Integer id);

}
