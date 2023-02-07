package nus.iss.team2.ADProjectTECHS.Controller;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
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

    private Member currentMember;



    // READ
    @GetMapping(value = {"/",""})
    public String ViewCalendar(Model model) {
        String currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        } else {
            throw new RuntimeException("No User");
        }

        currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember == null) throw new RuntimeException("cannot find current member");

        List<MyCourse> myCourseList = myCourseService.getAllMyCourses();




        return "Feature3-Dashboard/calendar";
    }

    // CREATE
    @PostMapping("/create")
    public String CreateScheduleEvent(){
        return "";
    }

    // DELETE
    @PostMapping("/delete")
    public String DeleteScheduleEvent(){
        return "";
    }

    //UPDATE
    @PostMapping("/update")
    public String UpdateScheduleEvent(){
        return "";
    }






}
