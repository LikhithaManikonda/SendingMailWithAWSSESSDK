package com.lambda.sdk;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

public class ReplyMailToCustomer {
    void sendMailToCustomer(AmazonSimpleEmailService client, LambdaWithSDK.Request req) {

        /*  final String FROM = "vikas.mahendra@codr.co.in";

          final String TO ="registrar@codr.co.in";*/
        final String TO = req.getEmail();
        final String FROM = "contact@codr.co.in";
        // The subject line for the email.
        String SUBJECT = "Confirmation Mail";

        // The HTML body for the email.
        String HTMLBODY = "<h2>Thank you for Contacting us</h2>"
                + "<p>This email was sent for Confirmation";

        // The email body for recipients with non-HTML email clients.
        String TEXTBODY = "This email was sent for testing the Confirmation";

        try {
          /*  AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.AP_SOUTH_1).build();*/
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(TO))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(HTMLBODY))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(FROM);
            // Comment or remove the next line if you are not using a
            // configuration set
            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }
}
