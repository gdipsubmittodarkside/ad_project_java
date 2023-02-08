package nus.iss.team2.ADProjectTECHS.Controller;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;
import nus.iss.team2.ADProjectTECHS.Utility.MemberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private ScheduleEventService scheduleEventService;

    @Autowired
    private MyCourseService myCourseService;

    @Autowired
    private MemberService memberService;



    private Long userId = -1l;



    // READ
    @GetMapping(value = {"/",""})
    public String ViewCalendar(Model model) {

        // from spring security context get member

        String currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        } else {
            throw new RuntimeException("No User");
        }

        if (userId < 0) {
            String username = MemberUtils.getMemberFromSpringSecurity();
            Member currentMember = memberService.loadMemberByUsername(username);
            userId = currentMember.getMemberId();
        }

        Member currentMember = memberService.findById(userId);
        List<ScheduleEvent> scheduleEventList = scheduleEventService.findScheduleEventByMemberId(userId);
        List<MyCourse> myCoursesS = new ArrayList<>();
        List<MyCourse> myCoursesUns = new ArrayList<>();
        List<MyCourse> myCourseList = myCourseService.getMyCoursesByMemberId(userId);


        for (int i = 0; i < scheduleEventList.size(); i++) {
            myCoursesS.add(scheduleEventList.get(i).getMyCourse());
        }

        for (int i = 0; i < myCourseList.size(); i++) {
            if (myCourseList.get(i).getScheduleEvent()==null) {
                myCoursesUns.add(myCourseList.get(i));
            }
        }

        model.addAttribute("unscheduledCourseList", myCoursesUns);
        model.addAttribute("scheduledCourseList", myCoursesS);


        return "Feature3-Dashboard/calendar";
    }

    // CREATE
    @PostMapping("/create")
    public String CreateScheduleEvent(@RequestParam("s") String startDate,
                                      @RequestParam("e") String endDate,
                                      @RequestParam("c") Long courseId){

        MyCourse myCourse = myCourseService.findMyCourseById(courseId);
        startDate = startDate.substring(0, startDate.indexOf("T"));
        endDate = endDate.substring(0, endDate.indexOf("T"));
        LocalDate sd = LocalDate.parse(startDate);
        LocalDate ed = LocalDate.parse(endDate);

        ScheduleEvent scheduleEvent = new ScheduleEvent();
        scheduleEvent.setMember(memberService.findById(userId));
        scheduleEvent.setStartDate(sd);
        scheduleEvent.setEndDate(ed);
        scheduleEvent.setMyCourse(myCourse);
        scheduleEventService.createScheduleEvent(scheduleEvent);

        return "redirect:/calendar";
    }

    // DELETE
    @PostMapping("/delete")
    public String DeleteScheduleEvent(@RequestParam("c") String courseTitle){

        MyCourse myCourse = myCourseService.findMyCourseByTitle(courseTitle);
        List<ScheduleEvent> scheduleEventList = scheduleEventService.findScheduleEventByMemberId(userId);
        for (int i = 0; i < scheduleEventList.size(); i++) {
            if (scheduleEventList.get(i).getMyCourse().equals(myCourse));
            scheduleEventService.deleteScheduleEvent(scheduleEventList.get(i).getScheduleId());
        }

        return "redirect:/calendar";
    }

    //UPDATE
    @PostMapping("/update")
    public String UpdateScheduleEvent(){
        return "";
    }









}
