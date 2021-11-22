package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ArcaneUser addUser(ArcaneUser user) {
        return userRepository.save(user);
    }

    public ArcaneUser findUser(Long userID) {
        return userRepository.findById(userID).orElseThrow(() -> new RuntimeException("no user with this id"));
    }

    @Transactional
    public ArcaneUser updateUser(ArcaneUser arcaneUser) {
        userRepository.findById(arcaneUser.getID()).ifPresent(user -> {
            user.setEmail(arcaneUser.getEmail());
            user.setFirstName(arcaneUser.getFirstName());
            user.setLastName(arcaneUser.getLastName());
            user.setUserName(arcaneUser.getUserName());
            user.setEmailVerified(arcaneUser.getEmailVerified());
            user.setVerificationCode(arcaneUser.getVerificationCode());
            user.setBio(arcaneUser.getBio());
            user.setHashedPassword(arcaneUser.getHashedPassword());
            user.setSalt(arcaneUser.getSalt());
            user.setAvatarUrl(arcaneUser.getAvatarUrl());
        });
        Optional<ArcaneUser> arcaneUserOptional = userRepository.findById(arcaneUser.getID());
        return arcaneUserOptional.orElse(null);
    }

    public ArcaneUser findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("no user with this user name"));
    }
}
