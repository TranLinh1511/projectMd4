package ra.model.dao.IDAO;

import ra.model.dao.IGenericDAO;
import ra.model.entity.User;

public interface IUserDAO extends IGenericDAO<User, Integer> {
    boolean blockUser(Integer userId);

    void unBlockUser(Integer userId);
    User userLogin(String email, String password);
}
