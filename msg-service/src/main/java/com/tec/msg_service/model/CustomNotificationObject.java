package com.tec.msg_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CustomNotificationObject {

    private String target;
    private String title;
    private String body;



}
