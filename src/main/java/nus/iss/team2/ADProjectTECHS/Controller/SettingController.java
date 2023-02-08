package nus.iss.team2.ADProjectTECHS.Controller;


import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.JobService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MySkillService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JobService jobService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private MySkillService mySkillService;

    private Long userId = -1l;

    private PasswordEncoder passwordEncoder;

    public SettingController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"/",""})
    public String showSettingsPage(Model model){

        if (userId < 0) {
            Member currentMember = getMemberFromSpringSecurity();
            userId = currentMember.getMemberId();
        }


        model.addAttribute("member", new Member());
        List<Job> jobList = jobService.findAll();
        model.addAttribute("jobList", jobList);

        List<Skill> skillList = skillService.findAll();
        model.addAttribute("skillList", skillList);

        return "settings";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("member") Member newMember, RedirectAttributes redirectAttributes,Model model){
        Member currentMember = getCurrentMember(userId);

        if (newMember != null) {

            if (newMember.getUsername()!=null && newMember.getUsername().trim()!="") {
                currentMember.setUsername(newMember.getUsername());

            }

            if (newMember.getEmail() != null && newMember.getEmail().trim() != "" ) {
                currentMember.setEmail(newMember.getEmail());
            }

            if (newMember.getCurrentJobTitle()!=null && newMember.getCurrentJobTitle().trim()!="") {
                currentMember.setCurrentJobTitle(newMember.getCurrentJobTitle());
            }

            if (newMember.getBirthday()!=null) {
                currentMember.setBirthday(newMember.getBirthday());
            }

            if (newMember.getShortBio()!=null && newMember.getShortBio().trim()!="") {
                currentMember.setShortBio(newMember.getShortBio());
            }

            if (newMember.getDreamJob()!=null) {
                currentMember.setDreamJob(newMember.getDreamJob());
            }


            memberService.save(currentMember);

        }

        model.addAttribute("message", "saved profile");


        if (newMember.getUsername()!=null && newMember.getUsername().trim()!="") {

            return "redirect:/logout";
        }

        return "redirect:/settings";

    }

    @PostMapping("/information")
    public String updateInfo(@ModelAttribute("member") Member newMember, @RequestParam("skills") List<Integer> skillsIds , RedirectAttributes redirectAttributes, Model model){
        Member currentMember = getCurrentMember(userId);

        if (skillsIds.size()>0) {
            List<Skill> skillList = new ArrayList<>();
            for (int i = 0; i < skillsIds.size(); i++) {
                Skill skill = skillService.findSkillById(Long.valueOf(skillsIds.get(i)));
                MySkill mySkill = new MySkill();
                if (mySkillService.findMySkillByMemberAndSkill(currentMember, skill)!=null){
                    mySkill.setSkill(skill);
                    mySkill.setMember(currentMember);
                    mySkillService.save(mySkill);
                    currentMember.getMySkills().add(mySkill);
                }

            }
        }




        if (newMember!=null) {

            if (newMember.getGender() != null) {
                currentMember.setGender(newMember.getGender());
            }
            if (newMember.getEducation() != null) {
                currentMember.setEducation(newMember.getEducation());
            }

            memberService.save(currentMember);
        }

        model.addAttribute("message", "save info");

        return "redirect:/settings";

    }


    @PostMapping("/password")
    public String updatePassword(
            @RequestParam("curPwd")String curPwd,
            @RequestParam("newPwd")String newPwd,
            RedirectAttributes ra,
            Model model){
        Member currentMember = getCurrentMember(userId);

        if (curPwd.equals(newPwd)) {
            model.addAttribute("message", "Your new password must be different than the old one.");
            model.addAttribute("member" ,new Member());
            return "settings";

        }
        if (!passwordEncoder.matches(curPwd, currentMember.getPassword())) {
            model.addAttribute("message", "Your old password is incorrect.");
            model.addAttribute("member" ,new Member());
            return "settings";

        } else {

            memberService.changePassword(currentMember, newPwd);
            model.addAttribute("message","You have changed your password successfully. Please login again." );
            return "redirect:/logout";
        }
    }



    public Member getMemberFromSpringSecurity(){
        String currentUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        } else {
            throw new RuntimeException("No User");
        }

        return memberService.loadMemberByUsername(currentUsername);
    }


    public Member getCurrentMember(Long id) {
        return memberService.findById(id);
    }








}
