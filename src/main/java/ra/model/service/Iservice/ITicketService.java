package ra.model.service.Iservice;
import ra.dto.response.ResTicket;
import ra.dto.response.ResTicketDetail;
import ra.model.entity.Ticket;

import java.util.List;

public interface ITicketService {
    List<ResTicket> getAll();

    int save(Ticket t);

    boolean delete(Integer id);

    ResTicket getById(Integer id);

    List<ResTicket> getTicketByScheduleId(Integer id);
    void bindingUser(Integer userId, Integer ticketId);
    List<ResTicketDetail> getTicketByUserId(Integer userId);
}
