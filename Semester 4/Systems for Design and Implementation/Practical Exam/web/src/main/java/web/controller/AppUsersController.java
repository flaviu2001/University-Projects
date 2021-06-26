package web.controller;

import core.domain.AppUser;
import core.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.dto.*;

import java.util.stream.Collectors;

@RestController
public class AppUsersController {
    public static final Logger logger = LoggerFactory.getLogger(AppUsersController.class);

    @Autowired
    private AppUserService appUserService;

    @RequestMapping(value = "/user-hello/{id}", method = RequestMethod.POST)
    AppUserDTO getUser(@PathVariable Long id) {
        logger.trace("getUser - method entered with " + id);
        AppUser user = appUserService.find(id);
        return new AppUserDTO(user.getId(), user.getName(), user.getBirthday(), null, null, null);
    }

    @RequestMapping(value = "/user-followers/{id}", method = RequestMethod.GET)
    AppUserDTO getUsers2(@PathVariable Long id) {
        logger.trace("getUsers2 - method entered with " + id);
        AppUser user = appUserService.findWithFollowers(id);
        return new AppUserDTO(user.getId(), user.getName(), user.getBirthday(), null, user.getFollowers().stream().map(follower ->
                new FollowerDTO(follower.getId(), follower.getName(), null, null))
                .collect(Collectors.toSet()), null);
    }

    @RequestMapping(value = "/user-posts/{id}", method = RequestMethod.GET)
    AppUserDTO getUsers3(@PathVariable Long id) {
        logger.trace("get users 3 - " + id);
        AppUser user = appUserService.findWithPostsAndFollowers(id);
        return new AppUserDTO(user.getId(), user.getName(), user.getBirthday(),
                null,
                user.getFollowers().stream().map(follower ->
                        new FollowerDTO(follower.getId(), follower.getName(), null, new AddressDTO(follower.getAddress().getId(), follower.getAddress().getCity(), null)))
                        .collect(Collectors.toSet()),
                user.getPosts().stream().map(post -> new PostDTO(post.getId(), post.getTitle(), null,
                        post.getComments().stream().map(comment -> new CommentDTO(comment.getId(), comment.getComment())).collect(Collectors.toList()))).collect(Collectors.toSet())
        );
    }
}
