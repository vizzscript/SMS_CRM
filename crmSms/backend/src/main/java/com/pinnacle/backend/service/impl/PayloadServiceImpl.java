package com.pinnacle.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinnacle.backend.dto.PayloadRequest;
import com.pinnacle.backend.dto.UserPayloadDTO;
import com.pinnacle.backend.service.PayloadService;
import com.pinnacle.backend.util.EncryptionUtil;

import lombok.extern.slf4j.Slf4j;

import com.pinnacle.backend.model.UserPayloadModel;
import com.pinnacle.backend.repository.UserPayloadModelRepository;

import reactor.core.publisher.Mono;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class PayloadServiceImpl implements PayloadService {
    // private static final Logger log =
    // LoggerFactory.getLogger(PayloadServiceImpl.class);
    private final WebClient webClient;

    // Constructor Injection: Spring will inject the WebClient.Builder
    public PayloadServiceImpl(WebClient.Builder webClientBuilder) {
        // Configure WebClient here, if needed
        this.webClient = webClientBuilder
                .build();
    }

    @Value("${api.key}")
    private String apiKey;

    @Value("${app.username}")
    private String userName;

    @Value("${app.password}")
    private String pwd;

    @Autowired
    private UserPayloadModelRepository repo;

    // Sending payload with apiKey as header & body as PayloadRequest dto
    @Override
    public Mono<String> sendPayload(
            @RequestHeader("API-Key") String apiKey, @RequestBody PayloadRequest payload) {
        try {
            // Convert payload to JSON or string format (you can use libraries like Jackson
            // or Gson)
            System.out.println(payload);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(payload);
            System.out.println(json);
            String payloadString = json.toString(); // Ideally, convert to JSON string

            // Encrypt the payload using EncryptionUtil
            String encryptedPayload = EncryptionUtil.encrypt(payloadString);

            String encryptedAPIKey = EncryptionUtil.encryptedAPIKey(apiKey);

            // You can return or further process the encrypted data (For example, send it
            // over the network)
            // return encryptedPayload;
            return webClient.post()
                    .uri("http://localhost:8080/client/payload/save")
                    .header("API-Key", encryptedAPIKey)
                    .bodyValue(encryptedPayload)
                    .retrieve()
                    .bodyToMono(String.class);

        } catch (Exception e) {
            // Handle exceptions properly
            e.printStackTrace();
            return Mono.error(new RuntimeException("Error encrypting data", e));
        }
    }

    @Override
    public Mono<String> sendPayloadData() {
        log.info("Starting to send payload data...");

        try {
            // Fetch and prepare payload
            Mono<PayloadRequest> payloadMono = Mono.fromSupplier(this::fetchUserPayload)
                    .map(this::createPayloadRequest);

            return payloadMono.flatMap(payload -> {
                try {
                    log.info("Payload fetched and ready for encryption...");

                    // Log the original payload before encryption
                    log.info("Original Payload: {}", payload);

                    // Important to convert payload from Object to JSON**
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(payload);

                    // Encrypt payload
                    String encryptedPayload = EncryptionUtil.encrypt(json.toString());
                    log.info("Payload encrypted successfully");
                    log.info("Encrypted Payload: {}", encryptedPayload);
                    log.info("API Key: {}", apiKey);
                    // Encrypt API Key
                    String encryptedAPIKey = EncryptionUtil.encryptedAPIKey(apiKey);
                    log.info("API Key encrypted successfully");
                    log.info("Encrypted API Key: {}", encryptedAPIKey);

                    // Set headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("responseType", "json");
                    headers.set("isEncrypted", "1");
                    headers.set("apiKey", encryptedAPIKey);
                    log.info("Headers set: responseType=json, isEncrypted=1");
                    log.info("Headers: {}", headers);

                    return sendPayloadToOrg(encryptedPayload, headers);
                } catch (Exception e) {
                    log.error("Error encrypting data", e);
                    return Mono.error(new RuntimeException("Error encrypting data", e));
                }
            });

        } catch (Exception e) {
            log.error("Error preparing data", e);
            return Mono.error(new RuntimeException("Error preparing data", e));
        }
    }

    // Fetch user payload from the database
    private List<UserPayloadModel> fetchUserPayload() {
        log.info("Fetching user payload from the database...");
        List<UserPayloadModel> userPayloadList = repo.findAll();
        log.info("Fetched Payload Data: {}", userPayloadList);
        return userPayloadList;
    }

    // Create formatted payload
    private PayloadRequest createPayloadRequest(List<UserPayloadModel> payloadData) {
        log.info("Creating payload request with the fetched data...");
        PayloadRequest payloadRequest = new PayloadRequest();
        payloadRequest.setUserName(userName);
        payloadRequest.setPassword(pwd);

        // Map UserPayloadModel to UserPayloadDTO, excluding the id
        List<UserPayloadDTO> cleanedPayloadData = payloadData.stream()
                .map(payload -> {
                    UserPayloadDTO cleanedPayload = new UserPayloadDTO();
                    cleanedPayload.setSender(payload.getSender());
                    cleanedPayload.setMobileNo(payload.getMobileNo());
                    cleanedPayload.setMessage(payload.getMessage());
                    return cleanedPayload;
                })
                .collect(Collectors.toList());

        payloadRequest.setPayload(cleanedPayloadData);
        log.info("Payload Request without IDs: {}", payloadRequest);
        return payloadRequest;
    }

    // Send encrypted payload to organization API
    private Mono<String> sendPayloadToOrg(String encryptedPayload, HttpHeaders headers) {
        log.info("Sending encrypted payload to organization API...");

        // Log payload and headers before sending
        log.info("Sending Payload: {}", encryptedPayload);
        log.info("Headers: {}", headers);

        return webClient
                .post()
                .uri("http://localhost:8080/client/payload/accept")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(encryptedPayload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Payload request finished"))
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException webClientException = (WebClientResponseException) e;
                        log.error("Failed to send data to the organization API. Status code: {}. Response body: {}",
                                webClientException.getStatusCode(), webClientException.getResponseBodyAsString());
                    } else {
                        log.error("Unexpected error occurred: ", e);
                    }
                    return Mono.just("Failed to send data: " + e.getMessage());
                });
    }

}