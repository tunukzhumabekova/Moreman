package com.ORT.controller;

import com.ORT.exception.AuthenticationException;
import com.ORT.exception.InvalidFormat;
import com.ORT.model.request.SignIn;
import com.ORT.model.request.SignUp;
import com.ORT.model.request.SignUpRequest;
import com.ORT.model.response.Authentication;
import com.ORT.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    @Operation(
            summary = "Endpoint for ADMIN"
    )
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp signUp, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthenticationException("All fields must be correct and not empty");
        }
        return ResponseEntity.ok(authenticationService.signUp(signUp));
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

    @PostMapping("/reset-password/code/{email}")
    @Operation(
            summary = "Request password reset code",
            description = """
                    This method can be used by everyone.
                    Sends a password reset code to the provided email address.
                    The code is sent only if the request is made after 5 minutes from the last reset request.
                    The code is valid for 30 minutes. If successful, an email with the reset code will be sent to the
                    user.
                    """
    )
    public ResponseEntity<String> getCode(@PathVariable String email) throws MessagingException {
        authenticationService.getCodeForResetPassword(email);
        return ResponseEntity.ok("The password reset code has been successfully sent to your email address.");
    }

    @PutMapping("reset/password/{email}")
    @Operation(
            summary = "Reset Password. Endpoint for USER",
            description = """
                    If the password reset was confirmed, then the user will be able to reset the password only within 30 minutes.
                    """
    )
    public ResponseEntity<String> resetPassword(@PathVariable String email,
                                                @RequestParam
                                                @NotBlank(message = "Password is mandatory")
                                                @Size(min = 6, message = "Password must be at least 6 characters long")
                                                String newPassword,
                                                @RequestParam
                                                @NotBlank(message = "Password is mandatory")
                                                @Size(min = 6, message = "Password must be at least 6 characters long")
                                                String confirmPassword) {
        if (!newPassword.equals(confirmPassword))
            throw new InvalidFormat("Passwords do not match.");
        authenticationService.resetPassword(email, newPassword, confirmPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PutMapping("confirm/reset/{email}")
    @Operation(
            summary = "Confirmation of password reset code. Endpoint for USER",
            description = """
                    This method checks the code, and if it matches the code that was sent to the user's email to
                    reset the password, then access is given to change the password, it only works for 30 minutes
                    """
    )
    public ResponseEntity<String> confirmCode(@PathVariable String email,
                                              @RequestParam String code) {
        authenticationService.confirmResetPassword(email, code);
        return ResponseEntity.ok("The password reset request has been confirmed.");
    }

    @PostMapping("register")
    @Operation(
            summary = "Registration. Endpoint for USER",
            description = """
                    The method registers the user, passwords must match and they must be at least 6 characters.
                    Age from 15 to 70 years is accepted. The full name should not contain symbols.
                    """
    )
    public Authentication registerUser(@Valid @RequestBody SignUpRequest signUp) {
        return authenticationService.register(signUp);
    }
}