package nus.iss.team2.ADProjectTECHS.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Repository.ScheduleEventRepository;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;


@Service
public class ScheduleEventServiceImpl implements ScheduleEventService{


    @Autowired
    ScheduleEventRepository seRepo; 

    @Override
    public List<ScheduleEvent> findScheduleEventByMemberId(Long memberId){

        return seRepo.findScheduleEventByMemberId(memberId);
    }

    @Override
    public ScheduleEvent findScheduleEventById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScheduleEvent createScheduleEvent(ScheduleEvent event) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScheduleEvent updateScheduleEvent(ScheduleEvent event) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean deleteScheduleEvent(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ScheduleEvent> getAllScheduleEvents() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
