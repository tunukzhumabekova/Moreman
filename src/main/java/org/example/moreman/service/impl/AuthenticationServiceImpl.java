package org.example.moreman.service.impl;

import com.agro.public_.tables.records.UserInfosRecord;
import lombok.extern.slf4j.Slf4j;
import org.example.moreman.config.JwtService;
import org.example.moreman.exception.AuthenticationException;
import org.example.moreman.model.request.SignIn;
import org.example.moreman.model.response.Authentication;
import org.example.moreman.repository.UserInfoRepository;
import org.example.moreman.repository.UserRepository;
import org.example.moreman.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, JwtService jwtService,
                                     PasswordEncoder passwordEncoder, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public Authentication signIn(SignIn signIn) throws AuthenticationException {
        UserInfosRecord userInfo = userInfoRepository.findByEmail(signIn.email());

        if (userInfo == null) {
            throw new AuthenticationException("User not found");
        }

        if (!passwordEncoder.matches(signIn.password(), userInfo.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        UserInfosRecord user = userRepository.findByUserInfoId(userInfo.getId());

        if (user == null) {
            throw new AuthenticationException("No user record found for the given user info");
        }

        String jwtToken = jwtService.generateToken(userInfo);
        return new Authentication(
                user.getId(),
                userInfo.getEmail(),
                jwtToken);
    }
}
