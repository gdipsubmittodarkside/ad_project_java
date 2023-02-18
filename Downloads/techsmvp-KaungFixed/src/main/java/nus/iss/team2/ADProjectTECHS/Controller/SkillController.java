package nus.iss.team2.ADProjectTECHS.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.CourseCrawledService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;

@Controller
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private CourseCrawledService courseCrawledService;

    @PostMapping("/searchIntro/{skillId}")
    @ResponseBody
    public List<String> showIntroduction(
            @ModelAttribute Skill skill,
            @PathVariable Long skillId, Model model) {

        Skill targetSkill = skillService
                .findSkillById(skillId);
        List<String> selectedSkill = new ArrayList<>();

        String separator = "=";
        String url = targetSkill.getUrlLink();
        int sepPos = url.indexOf(separator);
        String urlQuery = url
                .substring(sepPos + separator.length());

        String embedded = "https://www.youtube.com/embed/";
        String finalURL = embedded + urlQuery;

        selectedSkill.add(targetSkill.getSkillTitle());
        selectedSkill
                .add(targetSkill.getSkillDescription());
        selectedSkill.add(finalURL);

        return selectedSkill;
    }

    @GetMapping("/searchIntro/{skillId}")
    public String showCoursesOfOneSkill(
            @PathVariable("skillId") Long skillId,
            Model model) {

        Skill skill = skillService.findSkillById(skillId);

        if (skill == null)
            throw new RuntimeException(
                    "cannot find skill by skill Id");

        List<CourseCrawled> courseCrawledList = courseCrawledService
                .findCoursesBySkill(skill);

        model.addAttribute("courseList", courseCrawledList);

        return "Feature2-SearchCourse/course-result";

    }

}
