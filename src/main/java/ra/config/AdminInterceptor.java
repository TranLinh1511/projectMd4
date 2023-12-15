package ra.config;

import org.springframework.web.servlet.HandlerInterceptor;
import ra.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("adminLogin");
        if (user == null || !user.isRole()) {
            response.sendRedirect("/admin/login");
            return false;
        }
        return true;
    }
}
