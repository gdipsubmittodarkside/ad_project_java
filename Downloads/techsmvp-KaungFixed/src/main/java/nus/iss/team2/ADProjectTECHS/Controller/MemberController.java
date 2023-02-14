package nus.iss.team2.ADProjectTECHS.Controller;

import nus.iss.team2.ADProjectTECHS.Model.*;
import nus.iss.team2.ADProjectTECHS.Service.JobService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MySkillService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;
import nus.iss.team2.ADProjectTECHS.Utility.MemberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {
    /*  Includes:
        1. login
        2. logout
        3. sign up
        4. updateMember
    */

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberService memberService;


    @Autowired
    private JobService jobService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private MySkillService mySkillService;




    @GetMapping("/register")
    public String showRegistrationForm(Model model) {


        String currentUsername = MemberUtils.getMemberFromSpringSecurity();
        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember!=null) {
            if (currentMember.getUsername()!=null&&currentMember.getPassword()==null) {
                model.addAttribute("currentEmail", currentMember.getEmail());
                model.addAttribute("currentUsername", currentMember.getUsername());


            }
        }

        model.addAttribute("newMember", new Member());

        List<Job> jobList = jobService.findAll();
        model.addAttribute("jobList", jobList);
        List<Skill> skillList = skillService.findAll();
        model.addAttribute("skillList", skillList);




        return "Others/sign-up";
    }


    @PostMapping("/register")
    public String registerMemberAccount(@ModelAttribute("newMember") @Valid Member newMember, @RequestParam(value = "skills", required = false) List<Integer> skillsIds
            , BindingResult bindingResult) {


        String currentUsername = MemberUtils.getMemberFromSpringSecurity();
        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember!=null) {
            newMember.setUsername(currentMember.getUsername());
            newMember.setEmail(currentMember.getEmail());
            memberService.deleteMember(currentMember.getMemberId());
            memberService.createMember(newMember);
        } else {
            memberService.createMember(newMember);
        }

        if (skillsIds!=null){
            if (skillsIds.size()>0) {
                List<Skill> skillList = new ArrayList<>();
                for (int i = 0; i < skillsIds.size(); i++) {
                    Skill skill = skillService.findSkillById(Long.valueOf(skillsIds.get(i)));
                    MySkill mySkill = new MySkill();
                    if (mySkillService.findMySkillByMemberAndSkill(newMember, skill) == null){
                        mySkill.setSkill(skill);
                        mySkill.setMember(newMember);
                        mySkillService.save(mySkill);
                        if(newMember.getMySkills()==null){
                            newMember.setMySkills(new ArrayList<MySkill>());
                            newMember.getMySkills().add(mySkill);
                        } else {
                            newMember.getMySkills().add(mySkill);
                        };
                    }

                }
            }

        }



        return "redirect:/login";

    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id")Long id, Model model) {
        Member member = memberService.findById(id);
        if (member == null) throw new RuntimeException("cannot find this member");
        model.addAttribute("member", member);
        return "update-member";
    }

    @PutMapping("/members/update")
    public String updateMember(@ModelAttribute("member") @Valid Member member,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:";
        }

        memberService.updateMember(member);
        return "redirect:";


    }


    @PostMapping("/setdreamJob/{newDreamJobId}")
    @ResponseBody
    public void setDreamJob(@PathVariable String newDreamJobId){

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (currentMember == null) throw new RuntimeException("cannot find current member");

        Job dreamJob = jobService.findJobById(Long.parseLong(newDreamJobId));
        currentMember.setDreamJob(dreamJob);
        memberService.save(currentMember);

    }



}
