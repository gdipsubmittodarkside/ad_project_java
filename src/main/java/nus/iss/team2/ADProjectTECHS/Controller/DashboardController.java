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

import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;
import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.MySkillService;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;
import nus.iss.team2.ADProjectTECHS.Utility.MemberUtils;

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
       
                //find member
        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        
        if (currentMember == null) throw new RuntimeException("cannot find current member");

        //check if member have dream job or not 
        Job memberDreamJob = currentMember.getDreamJob();       
        if (memberDreamJob==null){
            return "redirect:/settings";
        }
        
         //mySkills (list)
         List <MySkill> mySkills = mySkillService.findMySkillByMemberId(currentMember.getMemberId());

         List<String> skillTitles = new ArrayList<>();
         List<MyCourse> myCourses = myCourseService.getMyCoursesByMemberId(currentMember.getMemberId());
 
     
         for(MySkill ms : mySkills){
             skillTitles.add(ms.getSkill().getSkillTitle());
         }
 
         List<Integer> progressList = new ArrayList<>();
         for(MyCourse mc : myCourses){
             progressList.add(mc.getProgress());
         }
 
         String dreamJob = currentMember.getDreamJob().getJobTitle();
        if(dreamJob.isEmpty()|| dreamJob==null){
          dreamJob = "EMPTY";
        }
       
         model.addAttribute("member", currentMember);
         model.addAttribute("skillTitles", skillTitles);
         model.addAttribute("myCourses", myCourses);
         model.addAttribute("progressList",progressList);
         model.addAttribute("dreamJobTitle",dreamJob);


         return "Feature3-Dashboard/dashboard";

    }


}
