package nus.iss.team2.ADProjectTECHS.security;

import lombok.AllArgsConstructor;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private MemberService memberService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberService.loadMemberByUsername(username);
        if (member == null) {
            throw  new UsernameNotFoundException("Member Not Found");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("member");
        authorities.add(authority);

        User userDetails = new User();
        userDetails.setAuthorities((List<GrantedAuthority>) authorities);
        userDetails.setUsername(username);
        userDetails.setId(member.getMemberId());
        userDetails.setPassword(member.getPassword());
        userDetails.setAvatar(member.getAvatar());
        return userDetails;
    }
}
