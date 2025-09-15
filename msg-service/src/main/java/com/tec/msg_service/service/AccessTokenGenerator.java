package com.tec.msg_service.service;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
public class AccessTokenGenerator {

     public String getAccessToken() throws IOException, IOException {
        String PATH = "C:\\Users\\samhi\\IdeaProjects\\note-app-services\\msg-service-security-filess\\notificationtesting-14145-firebase-adminsdk-fbsvc-e3ec992813.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(PATH))
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refresh();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
