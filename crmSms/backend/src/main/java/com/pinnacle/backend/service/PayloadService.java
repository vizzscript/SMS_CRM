package com.pinnacle.backend.service;


import com.pinnacle.backend.dto.PayloadRequest;

import reactor.core.publisher.Mono;


public interface PayloadService {
    // Function to send payload in encrypted format
    public Mono<String> sendPayload(String apiKey, PayloadRequest payloadRequest);

    public Mono<String> sendPayloadData();
}
