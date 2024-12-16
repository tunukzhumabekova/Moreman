package org.example.moreman.controller;


import org.example.moreman.service.impl.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/otp")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam String phone) {
        return paymentService.check(phone);
    }

    @GetMapping("/create")
    public ResponseEntity<?> createPayment(
            @RequestParam String phone,
            @RequestParam int amount,
            @RequestParam String quid,
            @RequestParam String comment) {
        return paymentService.createPaymentToSave(phone, amount, quid, comment);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @RequestParam String quid,
            @RequestParam String otp) {
        return paymentService.confirmPayment(quid, otp);
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkStatus(@RequestParam String quid) {
        return paymentService.checkStatus(quid);
    }
}
