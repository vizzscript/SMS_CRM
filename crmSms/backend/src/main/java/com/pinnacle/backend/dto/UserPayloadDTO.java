package com.pinnacle.backend.dto;

import lombok.Data;

@Data
public class UserPayloadDTO {
    private String sender;
    private String mobileNo;
    private String message;
}
