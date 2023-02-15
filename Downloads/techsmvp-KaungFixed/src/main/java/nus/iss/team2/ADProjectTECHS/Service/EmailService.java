package nus.iss.team2.ADProjectTECHS.Service;

import nus.iss.team2.ADProjectTECHS.Model.Data.EmailDetails;

public interface EmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
