package com.example.freeandnice.services.impl;

import com.example.freeandnice.dto.*;
import com.example.freeandnice.exceptions.EmailPinNotFoundException;
import com.example.freeandnice.exceptions.InvalidPasswordException;
import com.example.freeandnice.exceptions.InvalidPinException;
import com.example.freeandnice.exceptions.UserIsNotConfirmedByEmailException;
import com.example.freeandnice.models.ERole;
import com.example.freeandnice.models.EmailPin;
import com.example.freeandnice.models.User;
import com.example.freeandnice.repositories.EmailsPinsRepository;
import com.example.freeandnice.repositories.UserRepository;
import com.example.freeandnice.services.AuthenticationService;
import com.example.freeandnice.services.EmailService;
import com.example.freeandnice.services.JwtService;
import com.example.freeandnice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailsPinsRepository emailsPinsRepository;
    private final UserRepository userRepository;


    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        Optional<User> userOp = userRepository.findByEmailAndIsConfirmedFalse(request.getEmail());
        User user;

        if (userOp.isPresent()) {
            user = userOp.get();
            user.setUsername(request.getUsername());
            user.setFirstName(request.getFirstName());
            user.setMiddleName(request.getMiddleName());
            user.setLastName(request.getLastName());
            user.setAddress(request.getAddress());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setNewsSubscribed(request.getNewsSubscribed());
            user.setIsConfirmed(true);
        } else {
            user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .lastName(request.getLastName())
                    .address(request.getAddress())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(ERole.ROLE_USER)
                    .newsSubscribed(request.getNewsSubscribed())
                    .isConfirmed(true)
                    .createdAt(LocalDateTime.now())
                    .enabled(true)
                    .build();

            userService.create(user);
        }

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }



    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not fount by username: " + request.getEmail()));

        if (user.getIsConfirmed().equals(false)) throw new UserIsNotConfirmedByEmailException(user.getEmail());

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getEmail());

            var jwt = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(jwt);
        } else {
            throw new InvalidPasswordException(request.getEmail());
        }
    }

    @Override
    public void sendValidationMsgToEmail(String email) {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        emailService.sendSimpleEmail(
                email,
                "Подтверждение почты",
                "Для подтверждения почты введите пин код: " + randomNumber);
        Optional<EmailPin> emailPin = emailsPinsRepository.findByEmail(email);
        if (emailPin.isEmpty()) emailsPinsRepository.save(new EmailPin(email, randomNumber));
        else emailsPinsRepository.save(emailPin.get().setPin(randomNumber));
    }

    @Override
    public void validateEmail(String email, Long pin) {
        EmailPin emailPin = emailsPinsRepository.findByEmail(email)
                .orElseThrow(() -> new EmailPinNotFoundException(email));
        if (emailPin.getPin() == pin) {
            emailsPinsRepository.delete(emailPin);
        } else throw new InvalidPinException(pin);
    }

    @Override
    public Boolean doesUserAlreadyExist(String email) {
        if (!userRepository.existsByUsernameAndIsConfirmedTrue(email)) {
            sendValidationMsgToEmail(email);
        }
        return userRepository.existsByUsernameAndIsConfirmedTrue(email);
    }

    @Override
    public Long changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(changePasswordRequest.getUsername()));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            return userRepository.save(user).getId();
        } else {
            throw new InvalidPasswordException(user.getEmail());
        }
    }

    @Override
    public void forgotPassword(String email) {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        emailService.sendSimpleEmail(
                email,
                "Восстановление доступа к аккаунту",
                "Для смены пароля введите пин код: " + randomNumber);
        Optional<EmailPin> emailPin = emailsPinsRepository.findByEmail(email);
        if (emailPin.isEmpty()) emailsPinsRepository.save(new EmailPin(email, randomNumber));
        else emailsPinsRepository.save(emailPin.get().setPin(randomNumber));
    }

    @Override
    public Long changeForgottenPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(forgotPasswordRequest.getEmail()));
        user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
        return userRepository.save(user).getId();
    }
}