package com.tec.msg_service.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FCMService {


    @Autowired
    private AccessTokenGenerator accessTokenGenerator;

    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/notificationtesting-14145/messages:send";
    private String SERVER_KEY = ""; // from Firebase project settings

    private final OkHttpClient client = new OkHttpClient();



    public String sendNotification(String target, String title, String body) throws Exception {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String message = "";

        try{
            SERVER_KEY = accessTokenGenerator.getAccessToken();
            log.info("Server key "+SERVER_KEY);

        }catch (Exception e){
            log.error("Exception occured in generating access token in OAUTH:::" , e);
        }



        String json = "{"
                + "\"message\":{"
                + "   \"notification\":{"
                + "     \"title\":" + "\"" + title+ "\","
                + "     \"body\":" + "\"" + body + "\""
                + "   },"
                + "   \"token\":" + "\"" + target + "\""
                + "}"
                + "}";

        System.out.println("Json request:::"+json);
        RequestBody requestBody = RequestBody.create(json, JSON);


        Request request = new Request.Builder()
                .url(FCM_API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + SERVER_KEY)
                .addHeader("Content-Type", "application/json; UTF-8")
                .build();

        System.out.println("Request Body::: "+request);

        try (Response response = client.newCall(request).execute()) {
            System.out.println("FCM Response: " + response.body().string());

            if(response.isSuccessful()){
                message += "Success response from FCM";
            }else{
                message += "FCM failure response";
            }
        }catch (Exception e){
            log.error("Exception occured:::",e);
        }

        return  message;

    }

}
