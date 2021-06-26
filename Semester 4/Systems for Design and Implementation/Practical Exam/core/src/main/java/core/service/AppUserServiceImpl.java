package core.service;

import core.domain.AppUser;
import core.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService{
    public static final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUser find(Long id) {
        logger.trace("entered find with " + id);
        //noinspection OptionalGetWithoutIsPresent
        return appUserRepository.findById(id).get();
    }

    @Override
    public AppUser findWithFollowers(Long id) {
        logger.trace("entered find with followers with " + id);
        return appUserRepository.findWithFollowers(id);
    }

    @Override
    public AppUser findWithPostsAndFollowers(Long id) {
        logger.trace("entered find with posts and followers with " + id);
        return appUserRepository.findWithPostsAndFollowers(id);
    }
}
