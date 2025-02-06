package com.pinnacle.backend.service;


import org.springframework.http.ResponseEntity;

// import org.springframework.http.ResponseEntity;

import com.pinnacle.backend.dto.PayloadRequest;

import reactor.core.publisher.Mono;


public interface PayloadService {
    // Function to send payload in encrypted format
    public Mono<String> sendPayload(String apiKey, PayloadRequest payloadRequest);

    public Mono<ResponseEntity<String>> sendPayloadData();
}
