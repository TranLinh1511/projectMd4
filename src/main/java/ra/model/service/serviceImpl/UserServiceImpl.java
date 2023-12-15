package ra.model.service.serviceImpl;


import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.request.ReqUser;
import ra.model.dao.IDAO.IUserDAO;
import ra.model.entity.User;
import ra.model.service.Iservice.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    IUserDAO iUserDAO;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<User> getAll() {
        return iUserDAO.getAll();
    }

    @Override
    public int save(ReqUser t, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("userLogin");
        if (t.getId() == 0 || !t.getPassword().equals(user.getPassword())) {
            String BCryptPass = t.getPassword();
            BCryptPass = BCrypt.hashpw(BCryptPass, BCrypt.gensalt(12));
            t.setPassword(BCryptPass);
        }
        return iUserDAO.save(modelMapper.map(t, User.class));
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public User getById(Integer id) {
        return iUserDAO.getById(id);
    }

    @Override
    public boolean blockUser(Integer userId) {
        return iUserDAO.blockUser(userId);
    }

    @Override
    public void unBlockUser(Integer userId) {
        iUserDAO.unBlockUser(userId);
    }

    @Override
    public User login(String email, String password) {
        return iUserDAO.userLogin(email, password);
    }
}
