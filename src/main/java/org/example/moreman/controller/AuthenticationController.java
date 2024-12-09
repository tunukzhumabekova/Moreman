package org.example.moreman.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.moreman.model.request.SignIn;
import org.example.moreman.model.response.Authentication;
import org.example.moreman.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }



    @PostMapping("/sign-in")
    @Operation(
            summary = "Endpoint for ADMIN and USER"
    )
    public ResponseEntity<Authentication> signIn(@RequestBody SignIn signIn) {
        Authentication authResponse = authenticationService.signIn(signIn);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authResponse.token());

        return ResponseEntity.ok()
                .headers(headers)
                .body(authResponse);
    }


}