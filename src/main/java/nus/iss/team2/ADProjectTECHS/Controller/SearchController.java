package nus.iss.team2.ADProjectTECHS.Controller;

import lombok.RequiredArgsConstructor;
import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Enums.SearchType;
import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.CourseCrawledService;
import nus.iss.team2.ADProjectTECHS.Service.JobService;
import nus.iss.team2.ADProjectTECHS.Service.JobSkillService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nus.iss.team2.ADProjectTECHS.Model.Query;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobSkillService jobSkillService;

    @Autowired
    private CourseCrawledService courseCrawledService;



    @GetMapping("/skills/result")
    public String ViewSkillSearchResult(@RequestParam String job, Model model){

        Job job1 = jobService.findJobByJobTitle(job);
        List<Skill> skillList = skillService.findSkillsByJob(job1);
        model.addAttribute("skillList",skillList);


        return "Feature1-SearchSkills/skill-result";
    }

    @GetMapping("/skills/home")
    public String SearchSkill(Model model){
        List<Job> jobList = jobService.findAll();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < jobList.size(); i++) {
            titles.add(jobList.get(i).getJobTitle());
        }

        model.addAttribute("titles",titles);

        return "Feature1-SearchSkills/search";
    }

    @GetMapping("/courses/home")
    public String showSearchPage(Model model) {

        List<Skill> skillList = skillService.findAll();

        List<String> titles = new ArrayList<>();
        for (int i = 0; i < skillList.size(); i++) {
            titles.add(skillList.get(i).getSkillTitle());
        }
        model.addAttribute("titles", titles);

        return "Feature2-SearchCourse/search";
    }

    @GetMapping("/courses/result")
    public String getSearchResult(@RequestParam String skill, Model model){

        Skill skill1 = skillService.findSkillByTitle(skill);

        List<CourseCrawled> courseCrawledList = courseCrawledService.findCoursesBySkillId(skill1.getSkillId());


        model.addAttribute("courseList", courseCrawledList);

        return "Feature2-SearchCourse/course-result";

    }

    public List<String> getAllJobsAndSkillTitles(){
        List<String> titles = new ArrayList<>();
        List<String> jobTitles = jobService.findJobTitles();
        List<String> skillTitles = skillService.findSkillTitles();
        titles.addAll(jobTitles);
        titles.addAll(skillTitles);
        return titles;
    }






    // TESTING WEB CLIENT
    // @Autowired
    // WebClient client;
    
    // @GetMapping("")
    // public void findEmployee(){
        
    //     Flux<Employee> empListFlux = client.get()
    //         .uri("allcourses")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .retrieve()
    //         .bodyToFlux(Employee.class);

    //     List<Employee> empList = empListFlux.collectList().block();

    //     Mono<Employee> emp = client.get()
    //         .uri("/employee/1")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .retrieve()
    //         .bodyToMono(Employee.class);
    //     Employee employee = emp.block();

    //     System.out.println(employee);
    //     empList.stream().forEach(System.out::println);
    // }


//    @GetMapping(value = {"/search","/","home"})
//    public String Search(Model model){
//        model.addAttribute("query", new Query());
//        return "search";
//    }
//
//    @PostMapping("/search")
//    public String SearchRedirect(@ModelAttribute Query query,
//                                 RedirectAttributes redirectAttributes){
//
//        SearchType searchType = query.getSearchType();
//        String input = query.getQueryString().toLowerCase();
//
//        if (searchType.equals(SearchType.JOBS)) {
//            redirectAttributes.addAttribute("jobTitleEntered", input);
//            return "redirect:/skills/searchByJobTitle";
//        } else if (searchType.equals(SearchType.COURSES)){
//            redirectAttributes.addAttribute("courseTitleEntered", input);
//            return "redirect:/courses/searchByCourseTitle";
//        }
//
//        return "redirect:/search";
//
//    }
    
}
