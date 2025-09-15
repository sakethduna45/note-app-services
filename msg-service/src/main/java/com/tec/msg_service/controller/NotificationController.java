package com.tec.msg_service.controller;

import lombok.extern.slf4j.Slf4j;
import com.tec.msg_service.model.CustomNotificationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.tec.msg_service.service.FCMService;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    FCMService fcmService;



    @PostMapping("/sendnot")
    public String sendNotification(@RequestBody CustomNotificationObject notificationObject){

        String message = "";
        try{

            log.debug("Notification object received to controller::::" + notificationObject.toString());

            message = fcmService.sendNotification(notificationObject.getTarget(),notificationObject.getTitle(),notificationObject.getBody());
            log.debug("Send notification called with response ::: " + message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return message;

    }

}
