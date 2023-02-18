package nus.iss.team2.ADProjectTECHS.Controller.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MyCourse;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Service.CourseCrawledService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;

@RestController
@RequestMapping("/api")
public class RMyCourseController {

    @Autowired
    private MyCourseService myCourseService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CourseCrawledService courseCrawledService;


        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
        @ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}", content = {
                @Content(examples = { @ExampleObject(value = "") }) }),
        @ApiResponse(responseCode = "404", description = "${api.response-codes.notFound.desc}", content = {
                @Content(examples = { @ExampleObject(value = "") }) }) })
        @GetMapping("/myCourses")
        public ResponseEntity<List<MyCourse>> getAllMyCourses() {

            try {
                List<MyCourse> myCourses = myCourseService.getAllMyCourses();
                if (myCourses.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(myCourses, HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }


        @PostMapping("saveMyCourse/{member_id}/{courseCrawled_id}")
        public ResponseEntity<Boolean> saveMyCourse(@PathVariable(value="member_id") Long member_id, 
                                                    @PathVariable(value="courseCrawled_id") Long course_id){

            Member member = memberService.findById(member_id);
            CourseCrawled courseCrawled = courseCrawledService.findCourseCrawledById(course_id);

            if (member == null || courseCrawled == null){
                return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String courseTitle = courseCrawled.getCourseTitle();
            Skill courseSkill = courseCrawled.getSkill();
            long skill_id = courseSkill.getSkillId();
            String courseUrl = courseCrawled.getUrlLink();
    
            MyCourse my_course = new MyCourse();
            my_course.setMyCourseTitle(courseTitle);
            my_course.setSkill(skill_id);
            my_course.setProgress(0);
            my_course.setCourseUrl(courseUrl);
            my_course.setMember(member);

            myCourseService.createMyCourse(my_course);

            return new ResponseEntity<>(true, HttpStatus.OK);
       }

       @GetMapping("/myCourses/{member_id}")
        public ResponseEntity<List<CourseCrawled>> getMyCourseByMemberId(@PathVariable("member_id") Long member_id) {
            
            // should add json ignore for fields scheduleEvent and member for MyCourse entity, such that there won't be nested json.
            /* 
            currently this is not so important as we are not returning a list of MyCourse.
            this is because mobile side need to display all the fields of course crawled for saved course page
            */ 
            List<MyCourse> myCourseList = myCourseService.getMyCoursesByMemberId(member_id);
            
            List<CourseCrawled> listOfCClinkedToMyCourses = myCourseList.stream()
                            .map(c -> courseCrawledService.findCourseCrawledByUrlAndSkillId(c.getCourseUrl(), c.getSkill()))
                            .collect(Collectors.toList());

            try {
                if(listOfCClinkedToMyCourses == null || listOfCClinkedToMyCourses.size() == 0){
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(listOfCClinkedToMyCourses, HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    // yt 18 feb
    @DeleteMapping("/myCourses/{member_id}/{youtubeId}/{skillId}")
    public ResponseEntity<Boolean> deleteMyCourse(@PathVariable("youtubeId") String youtubeId, 
                                                @PathVariable("skillId")  Long skillId,
                                                @PathVariable("member_id")  Long member_id) 
    {
        //https://www.youtube.com/watch?v=h0nxCDiD-zg

        String courseUrl = "https://www.youtube.com/watch?v=" + youtubeId;
        Boolean deleted = myCourseService.deleteMyCourseByMemberIdCourseUrlAndSkillId(member_id, courseUrl, skillId);

        if (deleted){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        }
        // try {
        //     var isRemoved = myCourseService.deleteMyCourse(id);
        //     if (!isRemoved) {
        //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        //     }
        //     return new ResponseEntity<>(id, HttpStatus.OK);
        // } catch (Exception e) {
        //     return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        // }
    }

}