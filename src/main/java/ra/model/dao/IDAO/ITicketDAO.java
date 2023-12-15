package ra.model.dao.IDAO;

import ra.dto.response.ResTicketDetail;
import ra.model.dao.IGenericDAO;
import ra.model.entity.Ticket;
import ra.model.entity.User;

import java.util.List;

public interface ITicketDAO extends IGenericDAO<Ticket, Integer> {
    List<Ticket> getTicketsByScheduleId(Integer id);

    void bindingUser(Integer userId, Integer ticketId);

    List<ResTicketDetail> getTicketByUserId(Integer id);
}
