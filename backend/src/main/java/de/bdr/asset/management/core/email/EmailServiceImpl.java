package de.bdr.asset.management.core.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendApprovalEmail(String managerEmail, String assetName, String employeeName, String approvalLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply@asset-booking-manager.com");
        message.setTo(managerEmail);
        message.setSubject("Approval needed. Booking for " + assetName);

        String emailBody = String.format(
                "Dear,\n\nEmployee %s has requested booking for %s.\n\n" +
                "Please approve or reject request by clicking on the link:\n%s\n\n" +
                "Best regards,\nAsset Booking Manager",
                employeeName, assetName, approvalLink
        );

        message.setText(emailBody);

        javaMailSender.send(message);
    }
}
