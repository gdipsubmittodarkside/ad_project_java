package nus.iss.team2.ADProjectTECHS.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Repository.MySkillRepository;
import nus.iss.team2.ADProjectTECHS.Service.MySkillService;

@Service
public class MySkillServiceImpl implements MySkillService {

    @Autowired
    MySkillRepository mySkillRepository;

    @Override
    public List<MySkill> findMySkillByMemberId(Long memberId) {
        return mySkillRepository.findMySkillByMemberId(memberId);
    }

    @Override
    public void save(MySkill mySkill) {
        mySkillRepository.save(mySkill);
    }

    @Override
    public MySkill findMySkillByMemberAndSkill(Member currentMember, Skill skill) {
        return mySkillRepository.findMySkillByMemberAndSkill(currentMember, skill).orElse(null);
    }

    @Override
    public MySkill findMySkillById(Long id) {
        // TODO Auto-generated method stub
        return mySkillRepository.findById(id).get();
    }

    @Override
    public MySkill createMySkill(MySkill mySkill) {
        // TODO Auto-generated method stub
        return mySkillRepository.save(mySkill);
    }

    @Override
    public MySkill updateMySkill(MySkill mySkill, Long id) {
        // TODO Auto-generated method stub
        MySkill ms = mySkillRepository.findById(id).get();
        ms.setCourseTaken(mySkill.getCourseTaken());
        ms.setMember(mySkill.getMember());
        ms.setSkill(mySkill.getSkill());

        return mySkillRepository.save(ms);
    }

    @Override
    public Boolean deleteMySkill(Long id) {
        // TODO Auto-generated method stub
        MySkill ms = mySkillRepository.findById(id).get();
        if (ms != null) {
            mySkillRepository.delete(ms);
            return true;
        }
        return false;
    }

    @Override
    public List<MySkill> getAllMySkills() {
        // TODO Auto-generated method stub
        return mySkillRepository.findAll();
    }

}
