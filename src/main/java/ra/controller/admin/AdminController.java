package ra.controller.admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.dto.request.*;
import ra.dto.response.*;
import ra.model.entity.*;
import ra.model.service.Iservice.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:config.properties")
@Controller
@RequestMapping("admin")
public class AdminController {
    @Value("${path}")
    private String path;
    @Autowired
    IUserService userService;

    @Autowired
    IProvinceService provinceService;

    @Autowired
    ICinemaService cinemaService;

    @Autowired
    IImageService imageService;

    @Autowired
    ITheaterService theaterService;

    @Autowired
    ISeatService seatService;

    @Autowired
    IMovieService movieService;

    @Autowired
    IScheduleService scheduleService;

    @Autowired
    ITicketService ticketService;

    @Autowired
    IUserService iUserService;
    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("")
    public String index(Model m) {
        List<User> users = userService.getAll();
        m.addAttribute("users", users);
        int totalUser = users.size();
        m.addAttribute("totalUser", totalUser);
        return "admin/list/customer-list";
    }

    @GetMapping("add-cinema")
    public String addCinema(Model m) {
        ReqCinema cinema = new ReqCinema();
        getProvinces(m);
        m.addAttribute("cinema", cinema);
        return "admin/form/add-cinema";
    }

    @GetMapping("list-cinema")
    public String listCinema(Model m) {
        List<ResCinema> cinemas = cinemaService.getAll();
        ReqCinema cinema = new ReqCinema();
        getProvinces(m);
        m.addAttribute("cinema", cinema);
        m.addAttribute("cinemasSize", cinemas.size());
        m.addAttribute("cinemas", cinemas);
        return "admin/list/cinema-list";
    }

    @GetMapping("add-theater")
    public String addTheater(Model m) {
        ReqTheater theater = new ReqTheater();
        getCinemas(m);
        m.addAttribute("theater", theater);
        return "admin/form/add-theater";
    }

    @GetMapping("add-schedule")
    public String addSchedule(Model m) {
        ReqSchedule schedule = new ReqSchedule();
        m.addAttribute("schedule", schedule);
        return getList(m);
    }

    @GetMapping("list-schedule")
    public String listSchedule(Model m) {
        List<ResSchedule> schedules = scheduleService.getAll();
        m.addAttribute("schedules", schedules);
        return "admin/list/schedule-list";
    }

    @GetMapping("add-movie")
    public String addMovie(Model m) {
        ReqMovie movie = new ReqMovie();
        m.addAttribute("movie", movie);
        getGenres(m);
        return "admin/form/add-movie";
    }

    @GetMapping("list-movie")
    public String listMovie(Model m) {
        List<ResMovie> movies = movieService.getAll();
        m.addAttribute("movies", movies);
        return "admin/list/movie-list";
    }

    @GetMapping("list-theater")
    public String listTheater(Model m) {
        List<ResTheater> theaters = theaterService.getAll();
        m.addAttribute("theaters", theaters);
        return "admin/list/theater-list";
    }

    @PostMapping("add-cinema")
    public String save(@Valid @ModelAttribute("cinema") ReqCinema cinema, BindingResult result, @RequestParam("uploadImage") List<MultipartFile> multipartFiles, Model m) {
        if (result.hasErrors() || multipartFiles.get(0).isEmpty()) {
            getProvinces(m);
            if (multipartFiles.get(0).isEmpty()) m.addAttribute("imageNull", true);
            if (cinema.getId() == 0) {
                return "admin/form/add-cinema";
            } else {
                return "admin/form/update-cinema";
            }
        }
        int cinemaId = cinemaService.save(cinema);
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            File file = new File(path + fileName);
            Image image = new Image();
            image.setCinemaId(cinemaId);
            image.setImageName(fileName);
            try {
                imageService.save(image);
                multipartFile.transferTo(file);
            } catch (IOException e) {
                System.out.println("Lỗi up ảnh");
            }
        }
        return "redirect:/admin/list-cinema";

    }

    @PostMapping("add-theater")
    public String saveTheater(@Valid @ModelAttribute("theater") ReqTheater theater, BindingResult result, @RequestParam("uploadImage") List<MultipartFile> multipartFiles, Model m) {
        if (result.hasErrors() || multipartFiles.get(0).isEmpty()) {
            getCinemas(m);
            if (multipartFiles.get(0).isEmpty()) m.addAttribute("imageNull", true);
            if (theater.getId() == 0) {
                return "admin/form/add-theater";
            } else {
                return "admin/form/update-theater";
            }
        }
        int theaterId = theaterService.save(theater);
        for (int i = 0; i <= theater.getTotalSeat(); i++) {
            Seat seat = new Seat();
            String seatNumber = generateSeatNumber(i);
            seat.setSeatNumber(seatNumber);
            if (theater.getId() == 0) {
                seat.setTheaterId(theaterId);
            } else {
                seat.setTheaterId(theater.getId());
            }
            seatService.save(seat);
        }
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            File file = new File(path + fileName);
            Image image = new Image();
            image.setTheaterId(theaterId);
            image.setImageName(fileName);
            try {
                imageService.save(image);
                multipartFile.transferTo(file);
            } catch (IOException e) {
                System.out.println("Lỗi up ảnh");
            }
        }
        return "redirect:/admin/list-theater";
    }

    @PostMapping("add-movie")
    public String saveMovie(
            @Valid @ModelAttribute("movie") ReqMovie movie,
            BindingResult result,
            @RequestParam("uploadImage") List<MultipartFile> imageFiles,
            Model m
    ) {
        if (result.hasErrors() || imageFiles.get(0).isEmpty()) {
            getGenres(m);
            if (imageFiles.isEmpty() || imageFiles.get(0).isEmpty()) {
                m.addAttribute("imageNull", true);
            }
            return "admin/form/add-movie";
        }
        int movieId = movieService.save(movie);
        movie.getGenresId().forEach(id -> movieService.bindMovieGenre(movie.getId() == 0 ? movieId : movie.getId(), id));
        imageFiles.forEach(file -> {
            String fileName = file.getOriginalFilename();
            saveFile(file, path + fileName);
            Image image = new Image();
            if (movie.getId() == 0) {
                image.setMovieId(movieId);
            } else {
                image.setMovieId(movie.getId());
            }
            image.setImageName(fileName);
            imageService.save(image);
        });
        return "redirect:/admin/list-movie";
    }


    @PostMapping("add-schedule")
    public String saveSchedule(
            @Valid @ModelAttribute("schedule") ReqSchedule schedule,
            @RequestParam(name = "theaterId", required = false) Integer theaterId,
            BindingResult result,
            Model m
    ) {
        if (result.hasErrors()) {
            return getList(m);
        }
        schedule.setTheaterId(theaterId);
        int scheduleId = scheduleService.save(schedule);
        Ticket ticket = new Ticket();
        ticket.setPrice(schedule.getPrice());
        if (schedule.getId() == 0) {
            ticket.setScheduleId(scheduleId);
        } else {
            ticket.setScheduleId(schedule.getId());
        }
        ResTheater theater = theaterService.getById(theaterId);
        for (Seat seat : theater.getSeats()) {
            ticket.setSeatId(seat.getId());
            ticketService.save(ticket);
        }
        return "redirect:/admin/list-schedule";
    }


    private String getList(Model m) {
        getMovie(m);
        Map<Integer, List<ResTheater>> cinemaTheaters = new HashMap<>();
        for (ResCinema cinema : getCinemas(m)) {
            cinemaTheaters.put(cinema.getId(), theaterService.getTheaterByCinemaId(cinema.getId()));
        }
        m.addAttribute("cinemaTheaters", cinemaTheaters);
        return "admin/form/add-schedule";
    }

    private void saveFile(MultipartFile file, String filePath) {
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            System.out.println("Lỗi up file");
        }
    }

    private static String generateSeatNumber(int seatIndex) {
        char columnChar = (char) ('A' + seatIndex / 9);
        int row = 1 + seatIndex % 9;
        return "" + columnChar + row;
    }

    private void getProvinces(Model m) {
        List<ResProvince> provinces = provinceService.getAll();
        m.addAttribute("provinces", provinces);
    }

    private List<ResCinema> getCinemas(Model m) {
        List<ResCinema> cinemas = cinemaService.getAll();
        m.addAttribute("cinemas", cinemas);
        return cinemas;
    }

    private void getGenres(Model m) {
        List<Genre> genres = movieService.getGenresData();
        m.addAttribute("genres", genres);
    }

    private void getMovie(Model m) {
        List<ResMovie> movies = movieService.getAll();
        m.addAttribute("movies", movies);
    }


    @GetMapping("block-user")
    public String blockUser(@RequestParam(name = "id") Integer userId, Model m) {
        userService.blockUser(userId);
        List<User> users = userService.getAll();
        m.addAttribute("users", users);
        int totalUser = users.size();
        m.addAttribute("totalUser", totalUser);
        return "redirect:/admin/";

    }

    @GetMapping("unblock-user")
    public String unBlockUser(@RequestParam(name = "id") Integer userId, Model m) {
        userService.unBlockUser(userId);
        List<User> users = userService.getAll();
        m.addAttribute("users", users);
        int totalUser = users.size();
        m.addAttribute("totalUser", totalUser);
        return "redirect:/admin/";
    }

    @GetMapping("update-cinema")
    public String updateCinema(Model m, @RequestParam(name = "id") Integer cinemaId) {
        ReqCinema cinema = modelMapper.map(cinemaService.getById(cinemaId), ReqCinema.class);
        getProvinces(m);
        m.addAttribute("cinema", cinema);
        return "admin/form/update-cinema";
    }

    @GetMapping("delete-cinema")
    public String deleteCinema(@RequestParam(name = "id") Integer cinemaId) {
        cinemaService.delete(cinemaId);
        return "redirect:/admin/list-cinema";

    }

    @GetMapping("update-theater")
    public String updateTheater(Model m, @RequestParam(name = "id") Integer theaterId) {
        ResTheater resTheater = theaterService.getById(theaterId);
        ReqTheater theater = modelMapper.map(resTheater, ReqTheater.class);
        theater.setTotalSeat(resTheater.getSeats().size());
        getCinemas(m);
        m.addAttribute("theater", theater);
        return "admin/form/update-theater";
    }

    @GetMapping("delete-theater")
    public String deleteTheater(@RequestParam(name = "id") Integer theaterId) {
        theaterService.delete(theaterId);
        return "redirect:/admin/list-theater";

    }

    @GetMapping("update-movie")
    public String updateMovie(Model m, @RequestParam(name = "id") Integer movieId) {
        ResMovie resMovie = movieService.getById(movieId);
        ReqMovie movie = modelMapper.map(resMovie, ReqMovie.class);
        for (Genre genre : resMovie.getGenres()) {
            movie.getGenresId().add(genre.getId());
        }
        m.addAttribute("movie", movie);
        getGenres(m);
        return "admin/form/update-movie";
    }

    @GetMapping("delete-movie")
    public String deleteMovie(@RequestParam(name = "id") Integer movieId) {
        movieService.delete(movieId);
        return "redirect:/admin/list-movie";
    }

    @GetMapping("update-schedule")
    public String updateSchedule(Model m, @RequestParam(name = "id") Integer scheduleId) {
        ResSchedule resSchedule = scheduleService.getById(scheduleId);
        ReqSchedule schedule = modelMapper.map(resSchedule, ReqSchedule.class);
        schedule.setShowTime(resSchedule.getTime());
        double price = 0;
        for (ResTicket ticket : resSchedule.getTickets()) {
            price = ticket.getPrice();
            break;
        }
        schedule.setPrice(price);
        m.addAttribute("schedule", schedule);
        getMovie(m);
        Map<Integer, List<ResTheater>> cinemaTheaters = new HashMap<>();
        for (ResCinema cinema : getCinemas(m)) {
            cinemaTheaters.put(cinema.getId(), theaterService.getTheaterByCinemaId(cinema.getId()));
        }
        m.addAttribute("cinemaTheaters", cinemaTheaters);
        return "admin/form/update-schedule";
    }

    @GetMapping("delete-schedule")
    public String deleteSchedule(@RequestParam(name = "id") Integer scheduleId) {
        scheduleService.delete(scheduleId);
        return "redirect:/admin/list-schedule";
    }

    @GetMapping("login")
    public String loginForm(Model m) {
        User user = new User();
        m.addAttribute("u", user);
        return "admin/login-page";
    }

    @PostMapping("login")
    public String login(@ModelAttribute("u") User user, HttpSession httpSession, HttpServletResponse response, Model m) {
        User adminLogin = iUserService.login(user.getEmail(), user.getPassword());
        if (adminLogin != null) {
            httpSession.setAttribute("adminLogin", adminLogin);
            Cookie cookie = new Cookie("userEmail", user.getEmail());
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
            return "redirect:/admin";
        } else {
            m.addAttribute("lg", true);
            return "admin/login-page";
        }
    }
}
