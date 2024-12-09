package com.ORT.service.impl;

import com.ORT.config.JwtService;
import com.ORT.exception.AuthenticationException;
import com.ORT.exception.InvalidFormat;
import com.ORT.exception.NotFoundException;
import com.ORT.model.request.SignIn;
import com.ORT.model.request.SignUp;
import com.ORT.model.request.SignUpRequest;
import com.ORT.model.response.Authentication;
import com.ORT.repository.SchoolRepository;
import com.ORT.repository.UserInfoRepository;
import com.ORT.repository.UserRepository;
import com.ORT.service.AuthenticationService;
import com.databil.mentormind.public_.enums.Role;
import com.databil.mentormind.public_.tables.records.UserInfosRecord;
import com.databil.mentormind.public_.tables.records.UsersRecord;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;
    private final SchoolRepository schoolRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, JwtService jwtService,
                                     PasswordEncoder passwordEncoder, UserInfoRepository userInfoRepository,
                                     JavaMailSender javaMailSender, ResourceLoader resourceLoader,
                                     SchoolRepository schoolRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
        this.javaMailSender = javaMailSender;
        this.resourceLoader = resourceLoader;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public Authentication signUp(SignUp signUp) {
        if (userRepository.existsByEmail(signUp.email())) {
            throw new AuthenticationException("Email already exists");
        }

        UserInfosRecord newUserInfo = new UserInfosRecord();
        newUserInfo.setEmail(signUp.email());
        newUserInfo.setPassword(passwordEncoder.encode(signUp.password()));
        newUserInfo.setRole(signUp.role());
        int userInfoId = userInfoRepository.save(newUserInfo);

        UsersRecord newUser = new UsersRecord();
        newUser.setName(signUp.firstName());
        newUser.setSurname(signUp.lastName());
        newUser.setUserInfoId(userInfoId);  // Ensure that userInfoId is set correctly
        int userId = userRepository.save(newUser, userInfoId); // Assuming save method returns the generated ID

        var jwtToken = jwtService.generateToken(newUserInfo);

        return new Authentication(
                userId,
                newUserInfo.getEmail(),
                jwtToken,
                newUserInfo.getRole(),
                newUser.getName()
        );
    }

    @Override
    public Authentication signIn(SignIn signIn) {
        UserInfosRecord userInfo = userInfoRepository.findByEmail(signIn.email());

        if (userInfo == null) {
            throw new AuthenticationException("User not found");
        }

        if (!passwordEncoder.matches(signIn.password(), userInfo.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        UsersRecord user = userRepository.findByUserInfoId(userInfo.getId()); // Assuming a method to find user by userInfoId

        String jwtToken = jwtService.generateToken(userInfo);
        return new Authentication(
                user.getId(),
                userInfo.getEmail(),
                jwtToken,
                userInfo.getRole(),
                user.getName()
        );
    }

    @Override
    public void getCodeForResetPassword(String email) throws MessagingException {
        if (userRepository.findUserByEmail(email) == null)
            throw new NotFoundException("User not found");
// Проверяем, можно ли отправить код для сброса пароля. Код можно отправлять только через 5 минут после последней отправки.
        UserInfosRecord userInfo = userInfoRepository.findByEmail(email);
        if (userInfo.getEndDate() != null) {
            Duration duration = Duration.between(LocalDateTime.now(), userInfo.getEndDate());
            long minutesDifference = duration.toMinutes();
            if (minutesDifference > 25)
                throw new InvalidFormat("You can only send the password reset after " + (minutesDifference - 25)
                        + " minutes");
        }

        try {
            String randomCode = randomCode();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");


            String htmlContent = loadHtmlTemplate("classpath:static-html/resetPassword.html");
            // Замена {code} на реальный код
            htmlContent = htmlContent.replace("{code}", randomCode);

            helper.setFrom("ORT.kg");
            helper.setSubject("Код для сброса пароля");
            helper.setTo(email);
            helper.setText(htmlContent.replace("{code}", randomCode), true); // true для HTML

            javaMailSender.send(mimeMessage);

            userInfoRepository.saveCode(email, randomCode);
        } catch (MessagingException e) {
            throw new MessagingException("Error sending code to email. Please try again later.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(String email, String newPassword, String confirmPassword) {
        if (userRepository.findUserByEmail(email) == null)
            throw new NotFoundException("User not found");
        UserInfosRecord userInfo = userInfoRepository.findByEmail(email);
//        Проверка срока действия кода для сброса пароля
        if (userInfo.getReset()==null || !userInfo.getReset())
            throw new InvalidFormat("You have not confirmed the password reset, ask for a code and enter it");
        Duration duration = Duration.between(LocalDateTime.now(), userInfo.getEndDate());
        long minutesDifference = duration.toMinutes();
        if (minutesDifference < 0)
            throw new InvalidFormat("The code has expired");
        try {
            userInfoRepository.resetPassword(email, passwordEncoder.encode(newPassword));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to change your password.");
        }
    }

    @Override
    public void confirmResetPassword(String email, String code) {
        if (userRepository.findUserByEmail(email) == null)
            throw new NotFoundException("User not found");
        UserInfosRecord userInfo = userInfoRepository.findByEmail(email);
        if (!userInfo.getCode().equals(code))
            throw new InvalidFormat("You entered the wrong password reset code.");
        try {
            userInfoRepository.confirmResetPassword(email);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to confirm your password.");
        }
    }

    @Override
    public Authentication register(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new AuthenticationException("Email already exists");
        }
        if (!signUpRequest.passwordOne().equals(signUpRequest.passwordTwo()))
            throw new InvalidFormat("The passwords do not match");

        UserInfosRecord userInfosRecord = new UserInfosRecord();
        userInfosRecord.setEmail(signUpRequest.email());
        userInfosRecord.setPassword(passwordEncoder.encode(signUpRequest.passwordOne()));
        userInfosRecord.setRole(Role.USER);
        int userInfoId = userInfoRepository.save(userInfosRecord);

        UsersRecord user = new UsersRecord();
        user.setAge(signUpRequest.age());
        user.setName(signUpRequest.fullName());
        user.setRegisterDate(LocalDateTime.now());
        int userId;

        int schoolId = signUpRequest.schoolId();

        try {
            schoolRepository.findById(schoolId).orElseThrow();
            userId = userRepository.save(user, userInfoId, schoolId);
        } catch (Exception ignore) {
            userId = userRepository.save(user, userInfoId);
        }

        var jwtToken = jwtService.generateToken(userInfosRecord);
        return new Authentication(
                userId,
                signUpRequest.email(),
                jwtToken,
                Role.USER,
                signUpRequest.fullName()
        );
    }

    private String randomCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%05d", random.nextInt(100000));
    }

    private String loadHtmlTemplate(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }
}
