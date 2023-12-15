package ra.controller.user;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.ReqUser;
import ra.dto.response.*;
import ra.model.entity.Seat;
import ra.model.entity.User;
import ra.model.service.Iservice.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    IUserService iUserService;

    @Autowired
    IScheduleService scheduleService;

    @Autowired
    ICinemaService cinemaService;

    @Autowired
    IProvinceService provinceService;

    @Autowired
    ITicketService ticketService;

    // regex
    @GetMapping("")
    public String index(Model m) {
        List<ResSchedule> schedules = scheduleService.getAll();
        m.addAttribute("schedules", schedules);
        return "cgv/index";
    }

    @GetMapping("login")
    public String login(Model m) {
        User user = new User();
        m.addAttribute("user", user);
        return "cgv/login";
    }

    @GetMapping("register")
    public String register(Model m) {
        ReqUser user = new ReqUser();
        m.addAttribute("user", user);
        return "cgv/register";
    }


    // logic
    @PostMapping("register")
    public String register(@Valid @ModelAttribute("user") ReqUser user, BindingResult result, Model m, HttpSession session) {
        List<User> resUsers = iUserService.getAll();
        boolean exist = false;
        for (User resUser : resUsers) {
            if (resUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                exist = true;
                break;
            }
        }
        if (result.hasErrors() || exist) {
            m.addAttribute("emailError", exist);
            return "cgv/register";
        }
        iUserService.save(user, session);
        return "redirect:/login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute("user") User user, HttpSession httpSession, HttpServletResponse response, Model m) {
        User userLogin = iUserService.login(user.getEmail(), user.getPassword());
        if (userLogin != null && !userLogin.isRole()) {
            httpSession.setAttribute("userLogin", userLogin);
            Cookie cookie = new Cookie("userEmail", user.getEmail());
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            m.addAttribute("lg", true);
            return "cgv/login";
        }
    }

    @GetMapping("movie-detail")
    public String movieDetail(@RequestParam(value = "id", required = false) Integer scheduleId, Model m) {
        ResSchedule schedule = scheduleService.getById(scheduleId);
        m.addAttribute("schedule", schedule);
        return "cgv/detail";
    }


    @GetMapping("add-to-cart")
    public String addToCart(@RequestParam(value = "id", required = false) Integer scheduleId, Model m) {
        ResSchedule schedule = scheduleService.getById(scheduleId);

        ResTheater theater = schedule.getTheater();
        List<ResTicket> tickets = schedule.getTickets();
        m.addAttribute("schedule", schedule);
        m.addAttribute("theater", theater);
        m.addAttribute("tickets", tickets);
        return "cgv/add-to-cart";
    }

    @GetMapping("list-firm")
    public String listFirm(Model m) {
        List<ResSchedule> schedules = scheduleService.getAll();
        m.addAttribute("schedules", schedules);
        return "cgv/now-showing";
    }

    @GetMapping("list-cinema")
    public String listCinema(Model m) {
        List<ResCinema> cinemas = cinemaService.getAll();
        m.addAttribute("cinemas", cinemas);
        List<ResProvince> provinces = provinceService.getAll();
        m.addAttribute("provinces", provinces);
        return "cgv/cinox";
    }

    @GetMapping("province")
    public String province(Model m, @RequestParam("id") Integer id) {
        List<ResCinema> cinemas = cinemaService.getByProvinceId(id);
        m.addAttribute("cinemas", cinemas);
        return "cgv/list-cinemas";
    }

    @GetMapping("account-detail")
    public String accountDetail(Model m, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("userLogin");
        ReqUser reqUser = modelMapper.map(user, ReqUser.class);
        m.addAttribute("user", reqUser);
        return "cgv/edit-account";
    }

    ModelMapper modelMapper = new ModelMapper();

    @PostMapping("account-detail/edit")
    public String save(@Valid Model m, HttpSession httpSession, @RequestParam(value = "current-password", required = false) String curPass
            , @RequestParam(value = "new-password", required = false) String newPass
            , @RequestParam(value = "confirmation", required = false) String cfPass,
                       @ModelAttribute("user") ReqUser user,
                       BindingResult result
    ) {
        if (!curPass.isEmpty()) {
            if (!BCrypt.checkpw(curPass, user.getPassword())) {
                m.addAttribute("cusPass", true);
                return "cgv/edit-account";
            }
            if (!newPass.equals(cfPass)) {
                m.addAttribute("cfPass", true);
                return "cgv/edit-account";
            } else {
                user.setPassword(newPass);
            }
        }
        if (result.hasErrors()) {
            return "cgv/edit-account";
        }
        iUserService.save(user, httpSession);
        httpSession.removeAttribute("user");
        return "redirect:/login";
    }

    @GetMapping("logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("userLogin");
        return "redirect:/login";
    }

    @GetMapping("add-to-cart/booking")
    public String booking(@RequestParam(value = "id", required = false) Integer id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("userLogin");
        ticketService.bindingUser(user.getId(), id);
        return "cgv/successful";
    }

    @GetMapping("account-detail/transition")
    public String transition(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("userLogin");
        List<ResTicketDetail> tickets = ticketService.getTicketByUserId(user.getId());
        model.addAttribute("tickets", tickets);
        return "cgv/history-transition";
    }


}
