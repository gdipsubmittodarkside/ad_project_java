package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    // to view details and watch a course
    @PostMapping("/{courseId}")
    public String watchCourse(@PathVariable String courseId, Model model){
        
        CourseCrawled courseSelected = courseCrawledService.findCourseCrawledById(Long.parseLong(courseId));
        model.addAttribute("course", courseSelected);
        return "watchCourse";
    }

    @RequestMapping(value="/save", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void SaveToMyCourses(@RequestBody CourseCrawled course, RedirectAttributes redirectAttributes){
    
        if (course != null){
            // String courseTitle = course.getCourseTitle();
            // Skill courseSkill = course.getSkill();
            // String courseUrl = course.getUrlLink();
    
            // MyCourse my_course = new MyCourse();
            // my_course.setMyCourseTitle(courseTitle);
            // my_course.setSkill(courseSkill);
            // my_course.setProgress(0);
            // my_course.setCourseUrl(courseUrl);
    
            // //get member from Httpsession, current hardcoded for member m1
            // String currentUsername = "";
    
            // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //  if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //      currentUsername = authentication.getName();
            // } else {
            //      throw new RuntimeException("No User");
            // }
     
            // Member member = memberService.loadMemberByUsername(currentUsername);
            // my_course.setMember(member);
    
            // myCourseService.createMyCourse(my_course);
        }

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

