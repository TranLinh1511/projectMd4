package ra.model.service.Iservice;

import ra.model.entity.Image;

import java.util.List;

public interface IImageService {
    List<Image> getAll();

    int save(Image t);

    boolean delete(Integer id);

    Image getById(Integer id);

    List<Image> getByCinemaId(Integer id);

    List<Image> getByTheaterId(Integer id);

    List<Image> getByMovieId(Integer id);
}
