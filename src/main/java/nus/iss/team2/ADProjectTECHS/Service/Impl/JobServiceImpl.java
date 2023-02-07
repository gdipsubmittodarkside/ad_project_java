package nus.iss.team2.ADProjectTECHS.Service.Impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Repository.JobRepository;
import nus.iss.team2.ADProjectTECHS.Service.JobService;



@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobRepository jRepo;
    @Override
    public Job findJobByJobTitle(String jobTitle){
        
        return jRepo.findJobByJobTitle(jobTitle).orElse(null);
    }

    @Override
    public List<Job> findJobByJobTitleLike(String jobTitle) {
        return jRepo.findAllByJobTitleContainingIgnoreCase(jobTitle);
    }

    @Override
    public List<String> findJobTitles() {
        List<Job> jobs = jRepo.findAll();
        List<String> titles = new ArrayList<>();


        for(Job j : jobs) {
            titles.add(j.getJobTitle());
        }
        return titles;

    }

    @Override
    public List<Job> findAll() {
        return jRepo.findAll();
    }

    @Override
    public Job findJobById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Job createJob(Job job) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Job updateJob(Job job, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteJob(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Job> getAllJobs() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
