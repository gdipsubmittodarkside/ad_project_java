package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.CourseCrawledService;
import nus.iss.team2.ADProjectTECHS.Service.JobService;
import nus.iss.team2.ADProjectTECHS.Service.JobSkillService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;
import nus.iss.team2.ADProjectTECHS.Utility.MemberUtils;

@Controller
@RequiredArgsConstructor
@RequestMapping(value={"/search", "/"})
public class SearchController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobSkillService jobSkillService;

    @Autowired
    private CourseCrawledService courseCrawledService;

    private List<CourseCrawled> currentList;
    @Autowired
    private MemberService memberService;

    @Autowired
    private MyCourseService myCourseService;



    @GetMapping("/skills/result")
    public String ViewSkillSearchResult(@RequestParam String job, Model model){

        Job job1 = jobService.findJobByJobTitle(job);
        List<Skill> skillList = skillService.findSkillsByJob(job1);

        //find member
        String currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        } else {

            //if not member
            model.addAttribute("skillList",skillList);
            return "Feature1-SearchSkills/skill-result";
        }

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember == null) throw new RuntimeException("cannot find current member");

        //if member
        model.addAttribute("skillList",skillList);
        model.addAttribute("member",currentMember);
        model.addAttribute("searchedjob",job);
        model.addAttribute("job1",job1);


        return "Feature1-SearchSkills/skill-result";
    }


    @GetMapping(value={"/skills/home", "","/"})
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
    public String getSearchResult(@RequestParam(value="skill") String skill, Model model){

        Skill skill1 = skillService.findSkillByTitle(skill);

        List<CourseCrawled> courseCrawledList = courseCrawledService.findCoursesBySkillId(skill1.getSkillId());

        List<String> strings = new ArrayList<>();
        model.addAttribute("courseList", courseCrawledList);
        model.addAttribute("entered", skill);

        this.currentList = courseCrawledList;

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if(currentMember != null && currentMember.getMyCourses() != null){
            model.addAttribute("myCourses", myCourseService.getMyCoursesByMemberId(currentMember.getMemberId()).stream().map(mc->mc.getCourseUrl()).toList());
        }
        else{
            model.addAttribute("myCourses",strings);
        }
        return "Feature2-SearchCourse/course-result";

    }

    @PostMapping("/fragment")
    public String getSearchResultFromHeader(@RequestParam(value="query") String query, RedirectAttributes redirectAttributes){
        
        Skill skill = skillService.findSkillByTitle(query);
        Job job = jobService.findJobByJobTitle(query);

        if (skill != null){
            redirectAttributes.addAttribute("skill", query);
           return "redirect:/courses/result";
        }
        if (job != null){
            redirectAttributes.addAttribute("job", query);
            return "redirect:/skills/result";
        }

        return ""; // need error mapping
    }

    @GetMapping("/sort/popular")
    public String sortByLikes(Model model){

        currentList.sort(Comparator.comparingInt(c -> (int) c.getLikes()));
        model.addAttribute("courseList", currentList);

        return "Feature2-SearchCourse/course-result";



    }

    @GetMapping("/sort/time")
    public String sortByPopular(Model model){

        currentList.sort(Comparator.comparing(CourseCrawled::getDate));
        model.addAttribute("courseList", currentList);

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

    @GetMapping("/duration/{i}")
    public String filterByVideoDuration(@PathVariable("i") int i, Model model) {

        List<CourseCrawled> result = new ArrayList<>();

        if (i == 1) {
            result = currentList.stream().filter(courseCrawled ->
                            courseCrawled.getDurationHours() < 1)
                    .collect(Collectors.toList());
        }

        if (i == 2) {
            result = currentList.stream().filter(courseCrawled ->
                            courseCrawled.getDurationHours() >= 1 && courseCrawled.getDurationHours() < 2)
                    .collect(Collectors.toList());
        }

        if (i == 3) {
            result = currentList.stream().filter(courseCrawled ->
                            courseCrawled.getDurationHours() >=2 )
                    .collect(Collectors.toList());
        }

        model.addAttribute("courseList", result);

        return "Feature2-SearchCourse/course-result";


    }
    
}
