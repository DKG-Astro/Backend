package com.astro.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

        private static final String SENDGRID_API_KEY = ""; // API key(we can change based on email)

        public void sendEmail(String toEmail, String username, String password) throws IOException {
            Email from = new Email("udaychowdhary743@gmail.com"); // must be verified in SendGrid
            String subject = "Welcome to Our IIA";
            Email to = new Email(toEmail);

            String contentText = "Hello,\n\n" +
                    "Here are your vendor login details:\n" +
                    "Vendor Id: " + username + "\n" +
                    "Password: " + password + "\n\n" +
                    "Thanks!"+
                    "IIA Group";
            Content content = new Content("text/plain", contentText);

            Mail mail = new Mail(from, subject, to, content);
            SendGrid sg = new SendGrid(SENDGRID_API_KEY);
            Request request = new Request();

            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
            } catch (IOException ex) {
                throw ex;
            }
        }


}
