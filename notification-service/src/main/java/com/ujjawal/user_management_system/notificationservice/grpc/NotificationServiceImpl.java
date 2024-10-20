package com.ujjawal.user_management_system.notificationservice.grpc;

import com.ujjawal.user_management_system.notificationservice.grpc.NotificationRequest;
import com.ujjawal.user_management_system.notificationservice.grpc.NotificationResponse;
import com.ujjawal.user_management_system.notificationservice.grpc.NotificationServiceGrpc;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.ujjawal.user_management_system.notificationservice.config.TwilioConfig;
import com.ujjawal.user_management_system.notificationservice.model.NotificationStatusModel;
import com.ujjawal.user_management_system.notificationservice.model.NotificationTemplateModel;
import com.ujjawal.user_management_system.notificationservice.repository.NotificationStatusRepository;
import com.ujjawal.user_management_system.notificationservice.repository.NotificationTemplateRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.twilio.type.PhoneNumber;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GrpcService
public class NotificationServiceImpl extends NotificationServiceGrpc.NotificationServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationStatusRepository notificationStatusRepository;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private TwilioConfig twilioConfig;

    @Override
    public void sendNotificationByIdentifier(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        System.out.println("Entered sendNotificationByIdentifier in NotificationServiceImpl");
        String mobile = request.getMobile();
        String email = request.getEmail();
        String slug = request.getSlug();
        Map<String, String> additionalData = request.getAdditionalDataMap();
        System.out.println("request: " + request);
        logger.info("Received notification request: mobile={}, email={}, slug={}, additionalData={}", 
                mobile, email, slug, additionalData);

        NotificationResponse.Builder responseBuilder = NotificationResponse.newBuilder();
        boolean mobileNotificationSent = false;

        Optional<NotificationTemplateModel> templateOptional = notificationTemplateRepository.findBySlug(slug);
        if (templateOptional.isEmpty()) {
            logger.error("Template not found for slug: {}", slug);
            responseBuilder.setNotificationStatus(false);
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        NotificationTemplateModel template = templateOptional.get();

        if (mobile != null && !mobile.isEmpty()) {
            mobileNotificationSent = sendMobileNotification(mobile, template.getSmsTemplate(), additionalData);
        }

        if (email != null && !email.isEmpty()) {
            logger.info("Email notification not implemented yet");
        }

        // Save notification status
        NotificationStatusModel statusModel = new NotificationStatusModel();
        statusModel.setId(UUID.randomUUID());
        statusModel.setMobileNumber(mobile);
        statusModel.setEmail(email);
        statusModel.setMobileNotificationSentAt(LocalDateTime.now());

        if (mobileNotificationSent) {
            statusModel.setMobileNotificationStatus(NotificationStatusModel.NotificationStatus.SENT);
            statusModel.setSendStatus(NotificationStatusModel.NotificationSendStatus.SENT);
        } else {
            statusModel.setMobileNotificationStatus(NotificationStatusModel.NotificationStatus.FAILED);
            statusModel.setSendStatus(NotificationStatusModel.NotificationSendStatus.FAILED);
        }

        // Add email status
        if (email != null && !email.isEmpty()) {
            statusModel.setEmailNotificationStatus(NotificationStatusModel.NotificationStatus.NOT_SENT);
        }

        notificationStatusRepository.save(statusModel);

        responseBuilder.setNotificationStatus(mobileNotificationSent);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private boolean sendMobileNotification(String mobile, String notificationTemplate, Map<String, String> additionalData) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        String updatedTemplate = replaceTemplateVariables(notificationTemplate, additionalData);

        try {
            PhoneNumber to = new PhoneNumber(mobile.startsWith("+") ? mobile : "+91" + mobile);
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
            Message message = Message.creator(to, from, updatedTemplate).create();

            logger.info("Notification sent successfully: {}", message.getSid());
            return true;
        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage());
            return false;
        }
    }

    private String replaceTemplateVariables(String template, Map<String, String> additionalData) {
        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement = additionalData.getOrDefault(variable, "");
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
