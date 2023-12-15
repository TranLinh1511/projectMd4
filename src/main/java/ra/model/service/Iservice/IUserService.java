package ra.model.service.Iservice;

import ra.dto.request.ReqUser;
import ra.model.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IUserService {
    List<User> getAll();

    int save(ReqUser t,HttpSession httpSession);

    boolean delete(Integer id);

    User getById(Integer id);

    boolean blockUser(Integer userId);

    void unBlockUser(Integer userId);

    User login(String email, String password);
}
