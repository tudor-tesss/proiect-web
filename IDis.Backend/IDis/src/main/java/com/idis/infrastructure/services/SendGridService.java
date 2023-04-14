package com.idis.infrastructure.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public final class SendGridService {
    private static final String apiKey = "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";
    private static final Email fromEmailAddress = new Email("noreply.idis@proton.me");
    private static final SendGrid sendGridService = new SendGrid(apiKey);

    public static void sendEmail(String toEmailAddress, String subject, String content) throws Exception {
        var to = new Email(toEmailAddress);
        var emailContent = new Content("text/plain", content);

        var mail = new Mail(fromEmailAddress, subject, to, emailContent);
        var request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            var response = sendGridService.api(request);

            if (response.getStatusCode() != 202) {
                throw new IOException("SendGrid.SendEmail.Failed");
            }
        }
        catch (Exception e) {
            throw e;
        }
    }
}
