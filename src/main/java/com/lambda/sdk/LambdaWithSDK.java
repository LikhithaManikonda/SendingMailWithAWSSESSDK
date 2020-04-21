package com.lambda.sdk;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.util.StringUtils;

public class LambdaWithSDK implements RequestHandler<LambdaWithSDK.Request,LambdaWithSDK.Response> {

    public Response handleRequest(Request request, Context context){
        Response response = new Response();
        LambdaLogger logger = context.getLogger();
        logger.log("####OutPut####"+"Hi "+request.getName() +"Your Registered email "+request.getEmail());
        if(StringUtils.isNullOrEmpty(request.getName()) || StringUtils.isNullOrEmpty(request.getEmail()) || StringUtils.isNullOrEmpty(request.getDescription()) || StringUtils.isNullOrEmpty(request.getPhone())) {
            response.setResponseMessage("Sorry, We couldn't process ur request, Please try again!!");
        }
        else {
            sendBusinessMail(request,response);
        }
        return response;
    }
    Response sendBusinessMail(Request req,Response response) {

        final String FROM = "contact@codr.co.in";
        final String TO = "registrar@codr.co.in";
        // The subject line for the email.
        String SUBJECT = "Amazon SES test (AWS SDK for Java)";

        // The HTML body for the email.
        String HTMLBODY = "<h2>Customer Information</h2>"
                + "<p>This email was sent for testing the Customer information is captured or not"
                + "<p>CustomerName: "+req.getName()
                + "<p>Email Address: "+req.getEmail()
                +"<p>Phone Number: "+req.getPhone()
                + "<p>Description: "+req.getDescription();

        // The email body for recipients with non-HTML email clients.
        String TEXTBODY = "This email was sent for testing the Customer information is captured or not";

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.AP_SOUTH_1).build();
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
            response.setResponseMessage("Congratulations!! We have submitted your data. We will get back to you. Please verify your email");
            ReplyMailToCustomer replyMailToCustomer = new ReplyMailToCustomer();
            replyMailToCustomer.sendMailToCustomer(client,req);
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
            response.setResponseMessage("Sorry,something went wrong at Server side, Please try again!!");
        }
        return response;
    }
    static class Request {

        public String name;
        public String email;
        public String phone;
        public String description;

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

    }
    static class Response {
        public String responseMessage;

        public Response(){}

        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
        }

    }
}

