package ra.model.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dto.response.ResTicket;
import ra.dto.response.ResTicketDetail;
import ra.model.dao.IDAO.IScheduleDAO;
import ra.model.dao.IDAO.ITicketDAO;
import ra.model.entity.Ticket;
import ra.model.service.Iservice.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements ITicketService {
    @Autowired
    IScheduleService scheduleService;
    @Autowired
    IMovieService movieService;
    @Autowired
    ICinemaService cinemaService;
    @Autowired
    ITheaterService theaterService;
    @Autowired
    ITicketDAO ticketDAO;

    @Autowired
    ISeatService iSeatService;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ResTicket> getAll() {
        return null;
    }

    @Override
    public int save(Ticket t) {
        return ticketDAO.save(t);
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public ResTicket getById(Integer id) {
        return null;
    }

    @Override
    public List<ResTicket> getTicketByScheduleId(Integer id) {
        List<Ticket> tickets = ticketDAO.getTicketsByScheduleId(id);
        return tickets.stream().map((i) -> {
            ResTicket resTicket = modelMapper.map(i, ResTicket.class);
            resTicket.setSeat(iSeatService.getById(i.getSeatId()));
            return resTicket;
        }).collect(Collectors.toList());
    }

    @Override
    public void bindingUser(Integer userId, Integer ticketId) {
        ticketDAO.bindingUser(userId, ticketId);
    }

    @Override
    public List<ResTicketDetail> getTicketByUserId(Integer id) {
        return ticketDAO.getTicketByUserId(id);
    }
}
