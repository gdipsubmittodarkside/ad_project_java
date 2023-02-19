package nus.iss.team2.ADProjectTECHS.Controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.JobService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MySkillService;
import nus.iss.team2.ADProjectTECHS.Service.SkillService;
import nus.iss.team2.ADProjectTECHS.Utility.MemberUtils;

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

    // private Long userId = -1l;


    private PasswordEncoder passwordEncoder;

    public SettingController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"/",""})
    public String showSettingsPage(Model model){


        //find member
        String currentUsername = MemberUtils.getMemberFromSpringSecurity();
        // kaung2.0
        Member currentMember = memberService.loadMemberByUsername(currentUsername);
        String notistatus = currentMember.getNotification();
        Job dreamJob = currentMember.getDreamJob();


        if (currentMember == null) 
            throw new RuntimeException("cannot find current member");


        model.addAttribute("member", new Member());

        List<Job> jobList = jobService.findAll();
       
        if(dreamJob == null){
            String dj = "nodj";
            model.addAttribute("dj", dj);
            model.addAttribute("jobList", jobList);
        }else{
            jobList.remove(dreamJob);
            model.addAttribute("jobList",jobList);
        }
        
        List<Skill> skillList = skillService.findAll();
        model.addAttribute("skillList", skillList);
        model.addAttribute("currentAvatar", currentMember.getAvatar());
        model.addAttribute("currentMember", currentMember); 
        model.addAttribute("notistatus", notistatus);
        return "Others/settings";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("member") Member newMember, RedirectAttributes redirectAttributes,Model model){
        // Member currentMember = getCurrentMember(userId);

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);


        if (newMember != null) {


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


        return "redirect:/settings";

    }

    @PostMapping("/information")
    public String updateInfo(@ModelAttribute("member") Member newMember, @RequestParam(value = "skills", required = false) List<Integer> skillsIds , RedirectAttributes redirectAttributes, Model model){
        // Member currentMember = getCurrentMember(userId);

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);

        if (skillsIds!=null) {
            if (skillsIds.size()>0) {
                List<Skill> skillList = new ArrayList<>();
                for (int i = 0; i < skillsIds.size(); i++) {
                    Skill skill = skillService.findSkillById(Long.valueOf(skillsIds.get(i)));
                    MySkill mySkill = new MySkill();
                    if (mySkillService.findMySkillByMemberAndSkill(currentMember, skill) == null){
                        mySkill.setSkill(skill);
                        mySkill.setMember(currentMember);
                        mySkillService.save(mySkill);
                        if(currentMember.getMySkills()==null){
                            currentMember.setMySkills(new ArrayList<MySkill>());
                            currentMember.getMySkills().add(mySkill);
                        } else {
                            currentMember.getMySkills().add(mySkill);
                        };
                    }

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
        // Member currentMember = getCurrentMember(userId);
        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);


        if (curPwd.equals(newPwd)) {

            ra.addFlashAttribute("message", "Your new password must be different than the old one.");
            return "redirect:/settings";

        }


        if (!passwordEncoder.matches(curPwd, currentMember.getPassword())) {
            ra.addFlashAttribute("message", "Your old password is incorrect.");
            return "redirect:/settings";

        } else {

            memberService.changePassword(currentMember, newPwd);
            ra.addAttribute("message","You have changed your password successfully. Please login again." );
            return "redirect:/logout";
        }
    }



    @RequestMapping("/image")
    public String uploadImage(Model model,@RequestParam("image")MultipartFile file) {

        String currentUsername = MemberUtils.getMemberFromSpringSecurity();

        Member currentMember = memberService.loadMemberByUsername(currentUsername);


        if(file.isEmpty()) throw new RuntimeException("upload image fail");

        // change to  your static path!!!
        String currentDir = System.getProperty("user.dir");
        String staticPath = currentDir + "/src/main/resources/static";
        String filename = file.getOriginalFilename();
        String url_path = "images/avatar" + File.separator + filename;
        String savePath = staticPath + File.separator + url_path;
        String visitPath = File.separator + url_path;


        File saveFile = new File(savePath);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }

        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentMember.setAvatar(visitPath);
        memberService.save(currentMember);

        if (!setAvatar(visitPath)){
            setAuth2Avatar(visitPath);
        }






        return "redirect:/settings";
    }



    public Boolean setAuth2Avatar(String visitPath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try{
            nus.iss.team2.ADProjectTECHS.security.CustomOAuth2User userDetails = (nus.iss.team2.ADProjectTECHS.security.CustomOAuth2User) authentication.getPrincipal();
            userDetails.setAvatar(visitPath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Boolean setAvatar(String visitPath) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try{
            nus.iss.team2.ADProjectTECHS.security.User userDetails = (nus.iss.team2.ADProjectTECHS.security.User) authentication.getPrincipal();
            userDetails.setAvatar(visitPath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
