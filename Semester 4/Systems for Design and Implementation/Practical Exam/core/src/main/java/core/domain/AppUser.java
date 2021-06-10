package core.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "appUsersWithFollowers", attributeNodes = @NamedAttributeNode(value = "followers")),
        @NamedEntityGraph(name = "appUsersWithFollowersAndPosts", attributeNodes = {
                @NamedAttributeNode(value = "followers", subgraph = "subgraph2"),
                @NamedAttributeNode(value = "posts", subgraph = "subgraph1")
        },
        subgraphs = {
                @NamedSubgraph(name="subgraph1", attributeNodes = @NamedAttributeNode(value = "comments")),
                @NamedSubgraph(name="subgraph2", attributeNodes = @NamedAttributeNode(value = "address"))
        })
})
@NoArgsConstructor
@Entity
public class AppUser extends BaseEntity<Long> {
    private String name;
    private String birthday;

    @OneToOne(fetch = FetchType.LAZY)
    private Address address;

    @OneToMany(mappedBy = "appUser", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Follower> followers;

    @OneToMany(mappedBy = "appUser", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Post> posts;

    public AppUser(Long id, String name, String birthday) {
        this.setId(id);
        this.name = name;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Follower> followers) {
        this.followers = followers;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AppUser && this.getId().equals(((AppUser) obj).getId());
    }
}
