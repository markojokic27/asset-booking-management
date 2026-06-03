package de.bdr.asset.management.core.email;

import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    // @Value("${spring.mail.username}")
    // private String senderEmail;

    @Async
    @SneakyThrows
    @Override
    public void sendApprovalEmail(String managerEmail, String assetName, String employeeName, String approvalLink) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@asset-booking-manager.com");
        // helper.setFrom(senderEmail);
        helper.setTo(managerEmail);
        helper.setSubject("Approval needed. Booking for " + assetName);

        String emailBody = String.format(
                "<p>Dear,</p>" +
                "<p>Employee <strong>%s</strong> has requested booking for <strong>%s</strong>.</p>" +
                "<p>Please approve or reject the request by clicking on the link below:</p>" +
                "<p><a href=\"%s\" style=\"color: #007bff; text-decoration: none; font-weight: bold;\">Click here to review the request</a></p>" +
                "<br><p>Best regards,<br>Asset Booking Manager</p>",
                employeeName, assetName, approvalLink
        );

        helper.setText(emailBody, true);

        mailSender.send(message);
    }

    @Async
    @SneakyThrows
    @Override
    public void sendStatusNotificationEmail(String toEmail, String assetName, String status) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@asset-booking-manager.com");
        // helper.setFrom(senderEmail);
        helper.setTo(toEmail);
        helper.setSubject(status + ": Booking for " + assetName);

        String emailBody = String.format(
                "<p>Dear,</p>" +
                "<p>Your request for booking asset <strong>'%s'<strong> has been <strong style=\\\"color: %s;\\\">%s</strong> by your manager.<p>" +
                "<br><p>Best regards,<br>Asset Booking Manager</p>",
                assetName, status.equalsIgnoreCase("APPROVED") ? "green" : "red", status.toLowerCase()
        );

        helper.setText(emailBody, true);

        mailSender.send(message);
    }
}
