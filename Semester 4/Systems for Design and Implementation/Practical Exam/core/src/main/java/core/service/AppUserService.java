package core.service;

import core.domain.AppUser;

public interface AppUserService {
    AppUser find(Long id);  // returns the user without relations (e.g. followers, posts)
    AppUser findWithFollowers(Long id); // the posts relation is  not fetched
    AppUser findWithPostsAndFollowers(Long id); // all relations are fetched
}
