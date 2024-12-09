package com.ORT.service;

import com.ORT.model.request.SignIn;
import com.ORT.model.request.SignUp;
import com.ORT.model.request.SignUpRequest;
import com.ORT.model.response.Authentication;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    Authentication signUp(SignUp signUp);

    Authentication signIn(SignIn signIn);

    void getCodeForResetPassword(String email) throws MessagingException;

    void resetPassword(String email, String newPassword, String confirmPassword);

    void confirmResetPassword(String email, String code);

    Authentication register(SignUpRequest signUpRequest);
}