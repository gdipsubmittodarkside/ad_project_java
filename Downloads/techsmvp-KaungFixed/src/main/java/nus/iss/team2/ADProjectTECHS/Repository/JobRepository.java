package nus.iss.team2.ADProjectTECHS.Repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nus.iss.team2.ADProjectTECHS.Model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {



    Optional<Job> findJobByJobTitle(String jobTitle);

    List<Job> findAllByJobTitleContainingIgnoreCase(String jobTitle);



}
