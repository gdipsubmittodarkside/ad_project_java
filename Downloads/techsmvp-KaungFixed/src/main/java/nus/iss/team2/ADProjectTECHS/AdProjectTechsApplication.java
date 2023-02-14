package nus.iss.team2.ADProjectTECHS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import nus.iss.team2.ADProjectTECHS.Model.Enums.Gender;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nus.iss.team2.ADProjectTECHS.Controller.DeveloperController.DeveloperController;
import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Enums.Education;
import nus.iss.team2.ADProjectTECHS.Model.Job;
import nus.iss.team2.ADProjectTECHS.Model.JobSkill;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;
import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Repository.CourseCrawledRepository;
import nus.iss.team2.ADProjectTECHS.Repository.JobRepository;
import nus.iss.team2.ADProjectTECHS.Repository.JobSkillRepository;
import nus.iss.team2.ADProjectTECHS.Repository.MemberRepository;
import nus.iss.team2.ADProjectTECHS.Repository.MyCourseRepository;
import nus.iss.team2.ADProjectTECHS.Repository.MySkillRepository;
import nus.iss.team2.ADProjectTECHS.Repository.ScheduleEventRepository;
import nus.iss.team2.ADProjectTECHS.Repository.SkillRepository;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AdProjectTechsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdProjectTechsApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
    @Bean
	CommandLineRunner loadData(CourseCrawledRepository ccRepo,
							   MemberService mRepo,
							   ScheduleEventRepository seRepo,
							   JobSkillRepository jsRepo,
							   JobRepository jRepo,
							   MySkillRepository msRepo,
							   MyCourseRepository mcRepo,
							   SkillRepository sRepo) {
		return (args) -> {
			
		};


	}}





