package nus.iss.team2.ADProjectTECHS.Controller;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;
import nus.iss.team2.ADProjectTECHS.Utility.MemberUtils;
import org.springframework.beans.factory.annotation.Autowired;
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




    // READ
    @GetMapping(value = {"/",""})
    public String ViewCalendar(Model model) {


        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember == null) throw new RuntimeException("cannot find current member");


        List<MyCourse> myCoursesS = new ArrayList<>();
        List<MyCourse> myCoursesUns = new ArrayList<>();
        List<MyCourse> myCourseList = myCourseService.getMyCoursesByMemberId(currentMember.getMemberId());

        for (int i = 0; i < myCourseList.size(); i++) {
            if (myCourseList.get(i).getScheduleEvent()!=null) {
                myCoursesS.add(myCourseList.get(i));
            } else {
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
                                      @RequestParam("c") String courseTitle){

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember == null) throw new RuntimeException("cannot find current member");


        MyCourse myCourse = myCourseService.findMyCourseByTitle(courseTitle);
        startDate = startDate.substring(0, startDate.indexOf("T"));
        endDate = endDate.substring(0, endDate.indexOf("T"));
        LocalDate sd = LocalDate.parse(startDate);
        LocalDate ed = LocalDate.parse(endDate);

        if (scheduleEventService.findScheduleEventByMemberAndMyCourse(memberService.findById(currentMember.getMemberId()), myCourse) == null) {
            ScheduleEvent scheduleEvent = new ScheduleEvent();
            scheduleEvent.setMember(memberService.findById(currentMember.getMemberId()));
            scheduleEvent.setStartDate(sd);
            scheduleEvent.setEndDate(ed);
            myCourse.setScheduleEvent(scheduleEvent);
            scheduleEvent.setMyCourse(myCourse);
            scheduleEventService.createScheduleEvent(scheduleEvent);
        }


        return "redirect:/calendar";
    }

    // DELETE
    @PostMapping("/delete")
    public String DeleteScheduleEvent(@RequestParam("s") String startDate,
                                      @RequestParam("e") String endDate,
                                      @RequestParam("c") String courseTitle){

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember == null) throw new RuntimeException("cannot find current member");


        Member cm = memberService.findById(currentMember.getMemberId());

        MyCourse myCourse = myCourseService.findMyCourseByTitle(courseTitle);

        ScheduleEvent se = scheduleEventService.findScheduleEventByMemberAndMyCourse(cm, myCourse);
        myCourse.setScheduleEvent(null);
        myCourseService.updateMyCourse(myCourse, myCourse.getMyCourseId());

        if (se == null) {
            throw new RuntimeException("cannot find xx");
        }

        scheduleEventService.delete(se);

        return "redirect:/calendar";
    }










}
