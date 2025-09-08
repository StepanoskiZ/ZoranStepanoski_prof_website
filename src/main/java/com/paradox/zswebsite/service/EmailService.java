package com.paradox.zswebsite.service;

import com.paradox.zswebsite.service.dto.ContactMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendContactFormEmail(ContactMessageDTO contactMessageDTO) {
        log.debug("Sending contact form email notification for message from: {}", contactMessageDTO.getVisitorEmail());
        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            // This is the destination where you receive the email. This is correct.
            mail.setTo("zstepanoski@gmail.com");

            // This sets the Reply-To header to the visitor's email. This is correct.
            mail.setReplyTo(contactMessageDTO.getVisitorEmail());

            // 3. MOST IMPORTANT CHANGE FOR MAILJET
            // This MUST be an email address you have verified as a "Sender"
            // in your Mailjet account dashboard.
            mail.setFrom("zstepanoski@gmail.com"); // e.g., "contact@yourwebsite.com" or "zstepanoski@gmail.com" if you've verified it.

            // The rest of the logic is correct, just updated to use the DTO
            mail.setSubject("New Visitor Message from " + contactMessageDTO.getVisitorName());

            String emailBody =
                "You have received a new message from your website contact form.\n\n" +
                "-----------------------------------------\n" +
                "Name: " +
                contactMessageDTO.getVisitorName() +
                "\n" +
                "Email: " +
                contactMessageDTO.getVisitorEmail() +
                "\n" +
                "-----------------------------------------\n\n" +
                "Message:\n" +
                contactMessageDTO.getMessage();

            mail.setText(emailBody);

            javaMailSender.send(mail);
            log.debug("Email sent successfully to zstepanoski@gmail.com via Mailjet");
        } catch (MailException e) {
            log.error("Failed to send email via Mailjet", e);
        }
    }
}
