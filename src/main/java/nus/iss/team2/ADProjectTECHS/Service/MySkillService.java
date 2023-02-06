package nus.iss.team2.ADProjectTECHS.Service;

import java.util.List;

import nus.iss.team2.ADProjectTECHS.Model.MySkill;


public interface MySkillService {
    MySkill findMySkillById(Long id);
    MySkill createMySkill(MySkill mySkill);
    MySkill updateMySkill(MySkill mySkill, Long id);
    Boolean deleteMySkill(Long id);
    List<MySkill> getAllMySkills();

    List<MySkill> findMySkillByMemberId(Long memberId);
}
