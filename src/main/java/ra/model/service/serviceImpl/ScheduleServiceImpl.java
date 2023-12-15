package ra.model.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.request.ReqSchedule;
import ra.dto.response.ResSchedule;
import ra.dto.response.ResTheater;
import ra.model.dao.IDAO.ICinemaDAO;
import ra.model.dao.IDAO.IMovieDAO;
import ra.model.dao.IDAO.IScheduleDAO;
import ra.model.dao.IDAO.ITheaterDAO;
import ra.model.entity.Schedule;
import ra.model.service.Iservice.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements IScheduleService {
    @Autowired
    IScheduleDAO scheduleDAO;
    @Autowired
    IMovieService movieService;
    @Autowired
    ICinemaService cinemaService;
    @Autowired
    ITheaterService theaterService;
    @Autowired
    ITicketService ticketService;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ResSchedule> getAll() {
        List<Schedule> schedules = scheduleDAO.getAll();
        return schedules.stream().map((i) -> {
            ResSchedule resSchedule = modelMapper.map(i, ResSchedule.class);
            resSchedule.setCinema(cinemaService.getById(i.getCinemaId()));
            resSchedule.setTheater(theaterService.getById(i.getTheaterId()));
            resSchedule.setMovie(movieService.getById(i.getMovieId()));
            resSchedule.setTickets(ticketService.getTicketByScheduleId(i.getId()));
            return resSchedule;
        }).collect(Collectors.toList());
    }

    @Override
    public int save(ReqSchedule t) {
        return scheduleDAO.save(modelMapper.map(t, Schedule.class));
    }

    @Override
    public boolean delete(Integer id) {
        return scheduleDAO.delete(id);
    }

    @Override
    public ResSchedule getById(Integer id) {
        Schedule schedule = scheduleDAO.getById(id);
        ResSchedule resSchedule = modelMapper.map(schedule, ResSchedule.class);
        resSchedule.setCinema(cinemaService.getById(schedule.getCinemaId()));
        resSchedule.setTheater(theaterService.getById(schedule.getTheaterId()));
        resSchedule.setMovie(movieService.getById(schedule.getMovieId()));
        resSchedule.setTickets(ticketService.getTicketByScheduleId(schedule.getId()));
        return resSchedule;
    }
}
