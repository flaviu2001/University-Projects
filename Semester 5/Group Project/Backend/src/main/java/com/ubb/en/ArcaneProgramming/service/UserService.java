package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.model.FriendInvitation;
import com.ubb.en.ArcaneProgramming.model.FriendInvitationStatus;
import com.ubb.en.ArcaneProgramming.repository.InvitationRepository;
import com.ubb.en.ArcaneProgramming.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;


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

    public List<FriendInvitation> getFriendsAndPendingInvites(String username) {
        ArcaneUser arcaneUser = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("no user with this user name"));
        List<FriendInvitation> toReturn = invitationRepository.findThatHasInviterOrInvited(arcaneUser.getUserName());
        toReturn.addAll(invitationRepository.findPendingInvitationsOfUser(arcaneUser.getUserName()));
        return toReturn;
    }

    public List<FriendInvitation> getAllInvitations(String username) {
        ArcaneUser arcaneUser = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("no user with this user name"));
        return invitationRepository.findByInvitedOrInviter(arcaneUser.getUserName());
    }

    public FriendInvitation addInvitation(FriendInvitation friendInvitation) {
        return invitationRepository.save(friendInvitation);
    }

    public List<ArcaneUser> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void acceptInvitation(FriendInvitation friendInvitation) {
        List<FriendInvitation> friendLookup = invitationRepository.findByInvitedAndInviter(
                friendInvitation.getInvited().getUserName(),
                friendInvitation.getInviter().getUserName());
        if (friendLookup.size() != 1)
            throw new RuntimeException("Invalid friend invitation");
        FriendInvitation actualFriendInvitation = friendLookup.get(0);
        actualFriendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
    }

    @Transactional
    public void rejectInvitation(FriendInvitation friendInvitation) {
        List<FriendInvitation> friendLookup = invitationRepository.findByInvitedAndInviter(
                friendInvitation.getInvited().getUserName(),
                friendInvitation.getInviter().getUserName());
        if (friendLookup.size() != 1)
            throw new RuntimeException("Invalid friend invitation");
        FriendInvitation actualFriendInvitation = friendLookup.get(0);
        actualFriendInvitation.setStatus(FriendInvitationStatus.REJECTED);
    }
}
