package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.ArrayList;
import java.util.List;

import nus.iss.team2.ADProjectTECHS.Service.PythonAPIService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;

@Controller
@RequestMapping("/myCourses")
public class MyCourseController {

    @Autowired
    private MyCourseService myCourseService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PythonAPIService pythonAPIService;
    private Member currentMember;

    @Autowired
    private SkillService skillService;
    
    // CREATE
    // Saving of new courses done at "CourseController"

    // READ
    @GetMapping("")
    public String ViewMyCourses(Model model){

        //hardCode MemberId , it suppose to get from session
        String currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        } else {
            throw new RuntimeException("No User");
        }
        currentMember = memberService.loadMemberByUsername(currentUsername);


        List<MyCourse> myCourseList = myCourseService.getMyCoursesByMemberId(currentMember.getMemberId());

        List<String> skillTitles = new ArrayList<>();
       


        for (MyCourse mc : myCourseList){

            skillTitles.add((skillService.findSkillById(mc.getSkill())).getSkillTitle());

        }

		model.addAttribute("skillTitles",skillTitles);


        // List<MyCourse> myCourseList = myCourseService.getAllMyCourses();
        model.addAttribute("myCourseList", myCourseList);
        return "myCourses";
    }

    // DELETE
    @PostMapping("/delete")
    public String DeleteMyCourse(){
        return "";
    }

    @GetMapping(value={"/watchCourse/{id}"})
    public String WatchCourseVideo(@PathVariable Long id, Model model){

        //find current selected course
        MyCourse course = myCourseService.findMyCourseById(id);

        //substring url for embedded purpose
        String separator = "=";
        String url = course.getCourseUrl();
        int sepPos = url.indexOf(separator);
        String urlQuery = url.substring(sepPos+separator.length());

        model.addAttribute("course", course);
        model.addAttribute("urlQuery",urlQuery);

        return "watchCourse";
    }

    // @GetMapping(value={"/watchCourse/{id}"})
    // public String WatchCourseVideo(@PathVariable Long id, Model model){

    //     //find current selected course
    //     MyCourse course = myCourseService.findMyCourseById(id);

    //     //substring url for embedded purpose
    //     String separator = "=";
    //     String url = course.getCourseUrl();
    //     int sepPos = url.indexOf(separator);
    //     String urlQuery = url.substring(sepPos+separator.length());

    //     model.addAttribute("course", course);
    //     model.addAttribute("urlQuery",urlQuery);

    //     return "watchCourse";
    // }

    // /myCourses/testPythonCourse/
    @Async
    @GetMapping(value={"/testPythonCourse/{keyword}"})
    public void pythonCallCourse(@PathVariable String keyword, Model model){
        try{
            pythonAPIService.getAPICourseData(keyword);
            System.out.println("Yay, got course data");
            wait(2000);
            // return "courseList";
        }catch(Exception e){
            System.out.println(e);
        }

        // return "courseList";

    }

    // /myCourses/testPythonJob/
    @Async
    @GetMapping(value={"/testPythonJob/{keyword}"})
    public void pythonCallJob(@PathVariable String keyword, Model model){
        try{
            pythonAPIService.getAPIJobData(keyword);
            System.out.println("Yay, got job data");
            wait(2000);
            // return "courseList";
        }catch(Exception e){
            System.out.println(e);
        }

        // return "courseList";

    }





}
