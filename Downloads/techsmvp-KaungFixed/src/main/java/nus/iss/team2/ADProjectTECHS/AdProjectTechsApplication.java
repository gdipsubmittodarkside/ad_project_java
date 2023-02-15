package nus.iss.team2.ADProjectTECHS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import nus.iss.team2.ADProjectTECHS.Repository.CourseCrawledRepository;
import nus.iss.team2.ADProjectTECHS.Repository.JobRepository;
import nus.iss.team2.ADProjectTECHS.Repository.JobSkillRepository;
import nus.iss.team2.ADProjectTECHS.Repository.MyCourseRepository;
import nus.iss.team2.ADProjectTECHS.Repository.MySkillRepository;
import nus.iss.team2.ADProjectTECHS.Repository.ScheduleEventRepository;
import nus.iss.team2.ADProjectTECHS.Repository.SkillRepository;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;

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

	// PUT IN YOUR OWN COMMAND LINE RUNNER 
	// TO SET MEMBER CORRECTLY VIA SPRING SECURITY --> USE MemberService.createMember()

	@Bean
	CommandLineRunner loadData(CourseCrawledRepository ccRepo,
			MemberService mService,
			ScheduleEventRepository seRepo,
			JobSkillRepository jsRepo,
			JobRepository jRepo,
			MySkillRepository msRepo,
			MyCourseRepository mcRepo,
			SkillRepository sRepo) {
		return (args) -> {

			//  List<String> skillList = DeveloperController.skillList;
			// List<String> skillDesList = DeveloperController.skillDescList;
			// List<String> skillUrlList = DeveloperController.skillURLList;

			// for(int i =0; i<skillList.size(); i++){
			// 	Skill skill = new Skill();
			// 	skill.setSkillTitle(skillList.get(i));
			// 	skill.setSkillDescription(skillDesList.get(i));
			// 	skill.setUrlLink(skillUrlList.get(i));
			// 	sRepo.save(skill);
			// }

			// Member m1 = new Member();
			// m1.setUsername("techs");
			// m1.setPassword("techs");
			// m1.setEmail("coolcool@gmail.com");
			// m1.setGender(Gender.MALE);
			// m1.setBirthday(LocalDate.of(1983, 5, 22));
			// m1.setCourseTaken("Java");
			// m1.setCurrentCity("Singapore");
			// m1.setEducation(Education.OTHERS);
			// m1.setShortBio("I am cool!");
			// m1.setCurrentJobTitle("Tax Accountant");
			// m1.setAvatar("https://robohash.org/reiciendisrerumin.png?size=50x50&set=set1");

			// mService.createMember(m1);
		
		};
	}


}





