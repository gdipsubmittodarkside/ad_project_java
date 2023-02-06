package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    // CREATE
    // Saving of new courses done at "CourseController"

    // READ
    @GetMapping("")
    public String ViewMyCourses(Model model){

        //hardCode MemberId , it suppose to get from session
        Member member = memberService.findById((long)1);
        // List<MyCourse> myCourseList = myCourseService.getAllMyCourses();
        List<MyCourse> myCourseList = myCourseService.getMyCoursesByMemberId(member.getMemberId());
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





}
