package nus.iss.team2.ADProjectTECHS.Controller;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.Query;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
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



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("newMember",new Member());

        return "sign-up";
    }




    @PostMapping("/register")
    public String registerMemberAccount(@ModelAttribute("newMember") @Valid Member newMember
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }
        try {
            memberService.createMember(newMember);
            return "redirect:/login";

        } catch (Exception e){
            return "sign-up";

        }
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


}
