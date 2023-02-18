package nus.iss.team2.ADProjectTECHS.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.JobSkill;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Repository.JobSkillRepository;
import nus.iss.team2.ADProjectTECHS.Service.JobSkillService;

@Service
public class JobSkillServiceImpl implements JobSkillService {

    @Autowired
    private JobSkillRepository jsRepo;

    @Override
    public List<JobSkill> findSkillsByJobId(Long jobId) {
        return jsRepo.findSkillsByJobId(jobId);
    }

    @Override
    public List<Skill> findSkillsReq(Long jobId) {
        List<Skill> skills = new ArrayList<>();
        List<JobSkill> jobSkills = findSkillsByJobId(jobId);
        for (JobSkill jsk : jobSkills) {
            Skill sk = jsk.getSkill();
            skills.add(sk);
        }

        return skills;
    };

    @Override
    public List<JobSkill> findJobSkillByJob(Job job) {
        return jsRepo.findByJob(job);
    }

    @Override
    public JobSkill findJobSkillById(Long id) {
        // TODO Auto-generated method stub
        return jsRepo.findById(id).get();
    }

    @Override
    public JobSkill createJobSkill(JobSkill jobSkill) {
        // TODO Auto-generated method stub
        return jsRepo.save(jobSkill);
    }

    @Override
    public JobSkill updateJobSkill(JobSkill jobSkill, Long id) {
        // TODO Auto-generated method stub
        JobSkill js = jsRepo.findById(id).get();
        js.setJob(jobSkill.getJob());
        js.setJobSkillId(jobSkill.getJobSkillId());
        js.setLastUpdatedDate(jobSkill.getLastUpdatedDate());
        js.setSkill(jobSkill.getSkill());

        return jsRepo.save(js);
    }

    @Override
    public Boolean deleteJobSkill(Long id) {
        // TODO Auto-generated method stub
        JobSkill js = jsRepo.findById(id).get();
        if (js != null) {
            jsRepo.delete(js);
            return true;
        }
        return false;
    }

    @Override
    public List<JobSkill> getAllJobSkill() {
        // TODO Auto-generated method stub
        return jsRepo.findAll();
    }

}
