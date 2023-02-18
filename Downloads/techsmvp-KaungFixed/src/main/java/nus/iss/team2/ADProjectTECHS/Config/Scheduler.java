package nus.iss.team2.ADProjectTECHS.Config;

import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.ScheduleEvent;
import nus.iss.team2.ADProjectTECHS.Model.Data.EmailDetails;
import nus.iss.team2.ADProjectTECHS.Service.EmailService;
import nus.iss.team2.ADProjectTECHS.Service.MemberService;
import nus.iss.team2.ADProjectTECHS.Service.MyCourseService;
import nus.iss.team2.ADProjectTECHS.Service.ScheduleEventService;

@Component
public class Scheduler {

    @Resource
    private MemberService memberService;

    @Resource
    private ScheduleEventService scheduleEventService;

    @Resource
    private MyCourseService myCourseService;

    @Resource
    private EmailService emailService;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    public Scheduler(MemberService memberService,
            ScheduleEventService scheduleEventService,
            MyCourseService myCourseService,
            EmailService emailService) {
        memberService = this.memberService;
        scheduleEventService = this.scheduleEventService;
        myCourseService = this.myCourseService;
        emailService = this.emailService;

    }

    @Scheduled(cron = "0 0 0 * * ?")
    // @Scheduled(cron = "*/10 * * * * *") FOR TESTING
    public void cronSendEmail() {
        List<Member> members = memberService
                .getAllMembers();

        for (Member member : members) {

            if (member.getScheduleEvents() != null && member
                    .getNotification().equals("on")) {

                List<ScheduleEvent> scheduleEvents = scheduleEventService
                        .findScheduleEventByMemberId(
                                member.getMemberId());
                String msgBody = "Hi "
                        + member.getUsername()
                        + ", you scheduled the below course(s) for today:"
                        + "\n\n";

                for (ScheduleEvent se : scheduleEvents) {

                    if (se.getStartDate()
                            .isBefore(LocalDateTime.now())
                            && LocalDateTime.now().isBefore(
                                    se.getEndDate())) {
                        msgBody += se.getMyCourse()
                                .getMyCourseTitle()
                                + "\n\n";
                    }
                }

                EmailDetails ed = new EmailDetails();
                ed.setSubject(
                        "TECHS - Schedule Events Reminder");
                ed.setRecipient(member.getEmail());
                ed.setMsgBody(msgBody
                        + "Click on this link to find out more: http://localhost:8080/calendar");
                String status = emailService
                        .sendSimpleMail(ed);
                System.out.println(status);

            } else {
                continue;
            }
        }

    }

}
