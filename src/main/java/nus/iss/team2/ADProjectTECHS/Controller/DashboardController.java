package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;
import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.MySkillService;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MySkillService mySkillService;

    @Autowired
    private MyCourseService myCourseService;

    @Autowired
    private ScheduleEventService scheduleEventService;

    private Member currentMember;

    @GetMapping("")
    public String viewDashBoard(Model model){
        //find member
        String currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        } else {
            throw new RuntimeException("No User");
        }

        currentMember = memberService.loadMemberByUsername(currentUsername);


        //mySkills (list)
        List <MySkill> mySkills = mySkillService.findMySkillByMemberId(currentMember.getMemberId());

        List<String> skillTitles = new ArrayList<>();
        List<MyCourse> myCourses = myCourseService.getMyCoursesByMemberId(currentMember.getMemberId());


        for(MySkill ms : mySkills){
            skillTitles.add(ms.getSkill().getSkillTitle());
        }

        model.addAttribute("member", currentMember);
        model.addAttribute("skillTitles", skillTitles);
        model.addAttribute("myCourses", myCourses);


        return "dashboard";



    }


}
