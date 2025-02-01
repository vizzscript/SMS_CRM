package com.pinnacle.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class PayloadRequest {
    private String userName;
    private String password;
    private List<UserPayloadDTO> payload;
}