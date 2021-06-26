package core.repository;

import core.domain.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserRepository extends BlogRepository<AppUser, Long> {
    @Query("select distinct user from AppUser user where user.id = :id")
    @EntityGraph(value = "appUsersWithFollowers", type = EntityGraph.EntityGraphType.LOAD)
    AppUser findWithFollowers(@Param("id")Long id);

    @Query("select distinct user from AppUser user where user.id = :id")
    @EntityGraph(value = "appUsersWithFollowersAndPosts", type = EntityGraph.EntityGraphType.LOAD)
    AppUser findWithPostsAndFollowers(@Param("id")Long id);
}