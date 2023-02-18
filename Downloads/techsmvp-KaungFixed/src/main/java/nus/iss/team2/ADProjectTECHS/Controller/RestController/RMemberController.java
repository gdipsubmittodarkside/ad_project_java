package nus.iss.team2.ADProjectTECHS.Controller.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;

@RestController
@RequestMapping("/api")
public class RMemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("createMemberFromMobile/{username}")
    public ResponseEntity<Long> createMember(
            @PathVariable(value = "username") String username) {

        Member new_member = memberService.createMember(
                new Member(username, "mobileuser"));
        long member_id = 0;
        new_member.setNotification("off");

        if (new_member.getUsername().equals(username)) {
            member_id = new_member.getMemberId();
        }

        return new ResponseEntity<>(Long.valueOf(member_id),
                HttpStatus.OK);

    }

}
