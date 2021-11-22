package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.ArcaneUserConverter;
import com.ubb.en.ArcaneProgramming.dto.ArcaneUserDto;
import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.service.MailService;
import com.ubb.en.ArcaneProgramming.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ArcaneUserDto> registerUser(@RequestBody ArcaneUserDto arcaneUserDto) {
        //Get an UserDto which contains: email, firstName, lastName, userName, password
        ArcaneUser user = new ArcaneUser();
        user.setEmail(arcaneUserDto.getEmail());
        user.setFirstName(arcaneUserDto.getFirstName());
        user.setLastName(arcaneUserDto.getLastName());
        user.setUserName(arcaneUserDto.getUserName());
        user.setEmailVerified(Boolean.FALSE);
        user.setVerificationCode((long) (Math.random() * (999999 - 100000 + 1) + 100000));
        user.setBio(arcaneUserDto.getBio());
        hashPassword(user, arcaneUserDto.getPassword());
        user = userService.addUser(user);
        MailService mailService = new MailService("VERIFICATION EMAIL", "Your verification code is:\n" + user.getVerificationCode(), user.getEmail());
        Thread thread = new Thread(mailService);
        thread.setDaemon(true);
        thread.start();
        return new ResponseEntity<>(ArcaneUserConverter.convertToDto(user), HttpStatus.OK);
    }

    @GetMapping("/verification/{userName}/{code}")
    public ResponseEntity<Boolean> verifyEmail(@PathVariable String userName, @PathVariable Long code) {
        ArcaneUser user = userService.findUserByUserName(userName);
        if (Objects.equals(code, user.getVerificationCode())) {
            user.setEmailVerified(Boolean.TRUE);
        }
        user = userService.updateUser(user);
        return new ResponseEntity<>(user.getEmailVerified(), HttpStatus.OK);
    }

    @GetMapping("/login/{userName}/{password}")
    public ResponseEntity<ArcaneUserDto> loginUser(@PathVariable String userName, @PathVariable String password) {
        try {
            ArcaneUser user = userService.findUserByUserName(userName);
            if (verifyPassword(user, password))
                return new ResponseEntity<>(ArcaneUserConverter.convertToDto(user), HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/change/{username}/{password}")
    public ResponseEntity<Boolean> changeFields(@PathVariable String username, @PathVariable String password, @RequestBody ArcaneUserDto arcaneUserDto) {
        try {
            ArcaneUser user = userService.findUserByUserName(username);
            if (verifyPassword(user, password)) {
                user.setBio(arcaneUserDto.getBio());
                user.setFirstName(arcaneUserDto.getFirstName());
                user.setUserName(arcaneUserDto.getUserName());
                user.setLastName(arcaneUserDto.getLastName());
                user.setAvatarUrl(arcaneUserDto.getAvatarUrl());
                if (arcaneUserDto.getPassword().length() >= 8)
                    hashPassword(user, arcaneUserDto.getPassword());
                if (!user.getEmail().equals(arcaneUserDto.getEmail())) {
                    user.setEmail(arcaneUserDto.getEmail());
                    user.setEmailVerified(false);
                    user.setVerificationCode((long) (Math.random() * (999999 - 100000 + 1) + 100000));
                    MailService mailService = new MailService("VERIFICATION EMAIL", "Your verification code is:\n" + user.getVerificationCode(), user.getEmail());
                    Thread thread = new Thread(mailService);
                    thread.setDaemon(true);
                    thread.start();
                }
                userService.updateUser(user);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<ArcaneUserDto> getUserByUsername(@PathVariable String username) {
        ArcaneUser user = userService.findUserByUserName(username);
        return new ResponseEntity<>(ArcaneUserConverter.convertToDto(user), HttpStatus.OK);
    }

    @SneakyThrows
    private Boolean verifyPassword(ArcaneUser user, String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), user.getSalt(), 65536, 128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        return Arrays.equals(hash, user.getHashedPassword());
    }

    @SneakyThrows
    private void hashPassword(ArcaneUser user, String password) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        user.setSalt(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        user.setHashedPassword(hash);
    }
}
