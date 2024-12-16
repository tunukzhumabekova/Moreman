package org.example.moreman.service.impl;

import org.example.moreman.model.response.OtpResponse;
import org.example.moreman.model.response.Payment;
import org.example.moreman.model.response.QuidResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://api.mbank.kg/v1/otp/";
    private final String authHeader = "f9a6e1058e49dd221c1427dffef5731e7e3af57edac8c4ca74b23c35bbe0e0158db55e1b7631636c723dfed751e2a7c0551d1278beeaddfa0bfe986ca271b780";


    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authenticate", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public ResponseEntity<?> check(String phone) {
        String url = baseUrl + "/check?phone=" + phone;
        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        OtpResponse otpResponse = restTemplate.exchange(url, HttpMethod.GET, request, OtpResponse.class).getBody();
        return ResponseEntity.ok(otpResponse);
    }

    public ResponseEntity<?> createPaymentToSave(String phone, int amount, String quid, String comment) {
        String url = baseUrl + "/create?phone=" + phone + "&amount=" + amount + "&quid=" + quid + "&comment=" + comment;
        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        OtpResponse otpResponse =  restTemplate.exchange(url, HttpMethod.GET, request, OtpResponse.class).getBody();
        return ResponseEntity.ok(otpResponse);
    }

    public ResponseEntity<?> confirmPayment(String quid, String otp) {
        String url = baseUrl + "/confirm?quid=" + quid + "&otp=" + otp;
        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        OtpResponse otpResponse =  restTemplate.exchange(url, HttpMethod.GET, request, OtpResponse.class).getBody();
        return ResponseEntity.ok(otpResponse);
    }

    public ResponseEntity<?> checkStatus(String quid) {
        String url = baseUrl + "/status?quid=" + quid;
        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        OtpResponse otpResponse = restTemplate.exchange(url, HttpMethod.GET, request, OtpResponse.class).getBody();
        return ResponseEntity.ok(otpResponse);
    }





}
