package org.example.moreman.service;


import org.example.moreman.exception.AuthenticationException;
import org.example.moreman.model.request.SignIn;
import org.example.moreman.model.response.Authentication;

public interface AuthenticationService {

    Authentication signIn(SignIn signIn) throws AuthenticationException;

}