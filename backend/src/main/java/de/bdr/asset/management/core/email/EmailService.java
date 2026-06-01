package de.bdr.asset.management.core.email;

/**
 * Email Service
 */
public interface EmailService {

    void sendApprovalEmail(String managerEmail, String assetName, String employeeName, String approvalLink);

    void sendStatusNotificationEmail(String toEmail, String assetName, String status);
}
