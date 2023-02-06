package nus.iss.team2.ADProjectTECHS.Controller;


import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.JobSkill;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Repository.JobSkillRepository;
import nus.iss.team2.ADProjectTECHS.Service.JobService;
import nus.iss.team2.ADProjectTECHS.Service.JobSkillService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search/skills")
public class SearchSkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobSkillService jobSkillService;

    @GetMapping("/result")
    public String ViewSkillSearchResult(@RequestParam String job, Model model){

        Job job1 = jobService.findJobByJobTitle(job);
        List<Skill> skillList = skillService.findSkillsByJob(job1);
        model.addAttribute("skillList",skillList);


        return "Feature1-SearchSkills/skill-result";
    }

    @GetMapping("/home")
    public String SearchSkill(Model model){
        List<String> titles = jobService.findJobTitles();
        model.addAttribute("titles",titles);
        return "Feature1-SearchSkills/search";
    }



}
