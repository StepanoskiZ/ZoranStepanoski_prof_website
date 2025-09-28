package com.paradox.zswebsite.service; // Use your actual package name

import com.paradox.zswebsite.service.dto.ContactMessageDTO;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.jhipster.config.JHipsterProperties;

@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JHipsterProperties jHipsterProperties;
    private final JavaMailSender javaMailSender;

    public EmailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
    }

    public void sendContactFormEmail(ContactMessageDTO contactMessageDTO) {
        log.debug("Attempting to send contact form email from: {}", contactMessageDTO.getVisitorEmail());

        // Get the 'from' and 'to' addresses from your application.yml
        final String mailFrom = jHipsterProperties.getMail().getFrom();
        //        final String mailFrom = "zstepanoski@gmail.com";
        final String mailTo = "zstepanoski@gmail.com";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // Set the recipient (you)
            message.setTo(mailTo);
            // Set the sender (must be a verified email in Mailjet)
            message.setFrom(mailFrom);
            // Set the reply to (a visitor)
            message.setReplyTo(contactMessageDTO.getVisitorEmail());
            // Set the subject
            message.setSubject("New Contact Form Submission from: " + contactMessageDTO.getVisitorName());

            // Build the email body
            String emailBody = String.format(
                "You have received a new message from your website contact form.\n\n" +
                "-----------------------------------------\n" +
                "Name: %s\n" +
                "Email (Reply-To): %s\n" +
                "-----------------------------------------\n\n" +
                "Message:\n%s",
                contactMessageDTO.getVisitorName(),
                contactMessageDTO.getVisitorEmail(),
                contactMessageDTO.getMessage()
            );

            message.setText(emailBody);

            javaMailSender.send(mimeMessage);
            log.info("Contact form email sent successfully to '{}'", mailTo);
        } catch (MailException | jakarta.mail.MessagingException e) {
            log.error("Email could not be sent to user '{}'", mailTo, e);
        }
    }
}
