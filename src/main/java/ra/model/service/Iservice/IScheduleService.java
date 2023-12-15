package ra.model.service.Iservice;

import ra.dto.request.ReqSchedule;
import ra.dto.response.ResSchedule;
import ra.model.entity.Schedule;

import java.util.List;

public interface IScheduleService {
    List<ResSchedule> getAll();

    int save(ReqSchedule t);

    boolean delete(Integer id);

    ResSchedule getById(Integer id);
}
