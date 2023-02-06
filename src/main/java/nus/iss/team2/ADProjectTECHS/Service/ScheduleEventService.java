package nus.iss.team2.ADProjectTECHS.Service;

import java.util.List;

import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;


public interface ScheduleEventService {
    ScheduleEvent findScheduleEventById(Long id);
    ScheduleEvent createScheduleEvent(ScheduleEvent event);
    ScheduleEvent updateScheduleEvent(ScheduleEvent event);
    Boolean deleteScheduleEvent(Long id);
    List<ScheduleEvent> getAllScheduleEvents();

    List<ScheduleEvent> findScheduleEventByMemberId(Long memberId);
}
