package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.CourseCrawledService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseCrawledService courseCrawledService;

    @Autowired
    private MyCourseService myCourseService;

    private List<CourseCrawled> currentList;

    @Autowired
    private MemberService memberService;


    // @GetMapping("/searchBySkillTitle")
    // public String FindCoursesBySkillTitle(@RequestParam String skillTitleEntered, Model model){
    //     List<CourseCrawled> courses = courseCrawledService.findCoursesBySkillTitleLike(skillTitleEntered);

    //     model.addAttribute("entered", skillTitleEntered);
    //     model.addAttribute("courseList", courses);
    //     currentList = courses;
    //     return findPaginated(1, 5, model);
    // }


    @GetMapping("/searchByCourseTitle")
    public String FindCoursesByCourseTitle(@RequestParam String courseTitleEntered, Model model){
        List<CourseCrawled> courses = courseCrawledService.findCoursesTitleLike(courseTitleEntered);

        model.addAttribute("entered", courseTitleEntered);
        model.addAttribute("courseList", courses);
        currentList = courses;
        return findPaginated(1,5, model);
    }



    @GetMapping("/save/{courseId}")
    public String SaveToMyCourses(@PathVariable String courseId, RedirectAttributes redirectAttributes){
    

    
        CourseCrawled course = courseCrawledService.findCourseCrawledById(Long.parseLong(courseId));

        String courseTitle = course.getCourseTitle();
        Skill courseSkill = course.getSkill();
        String courseUrl = course.getUrlLink();

        MyCourse my_course = new MyCourse();
        my_course.setMyCourseTitle(courseTitle);
        my_course.setSkill(courseSkill);
        my_course.setProgress(0);
        my_course.setCourseUrl(courseUrl);

        //get member from Httpsession, current hardcoded for member m1
        Member member = memberService.findById((long)1); 
        my_course.setMember(member);

        myCourseService.createMyCourse(my_course);

        redirectAttributes.addAttribute("courseTitleEntered",course.getCourseTitle());

        return "redirect:/courses/searchByCourseTitle";
      

    }




    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("pageSize") int pageSize,
                                Model model) {

        Page<CourseCrawled> courseCrawledPage = courseCrawledService.findPaginated(pageNo, pageSize);
        model.addAttribute("courses", courseCrawledPage);
        List<CourseCrawled> courseCrawledList = courseCrawledPage.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", courseCrawledPage.getTotalPages());
        model.addAttribute("totalItems", courseCrawledPage.getTotalElements());
        model.addAttribute("courses", courseCrawledList);

        return "course-result";

    }


    /*
    todo: finish the filter by video duration
    * */
    @GetMapping("/filterByVideoDuration/{duration}")
    public String filterByVideoDuration(@PathVariable(value = "duration") Long duration,Model model) {


        return findPaginated(1, 5, model);
        


    }


    @GetMapping("/sortByLikes")
    public String sortByLikes(Model model){

        currentList.sort((c1, c2) -> Math.toIntExact(c1.getLikes() - c2.getLikes()));
        model.addAttribute("courses", currentList);

        return findPaginated(1, 5, model);

    }

    

}

