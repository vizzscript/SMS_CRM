package com.pinnacle.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinnacle.backend.dto.PayloadRequest;
import com.pinnacle.backend.service.PayloadService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payload")
public class PayloadController {
    @Autowired
    private PayloadService payloadService;

    @PostMapping("/send")
    public Mono<String> sendPayload(@RequestHeader("API-Key") String apiKey, @RequestBody PayloadRequest payloadRequest) {
        return payloadService.sendPayload(apiKey, payloadRequest);
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<String>> sendPayloadData(){
        return payloadService.sendPayloadData();
    }
}
