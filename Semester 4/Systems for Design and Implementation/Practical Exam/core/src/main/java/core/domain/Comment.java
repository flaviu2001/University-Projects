package core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Entity
public class Comment extends BaseEntity<Long>{
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Comment(Long id, String comment) {
        this.setId(id);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Comment && this.getId().equals(((Comment) obj).getId());
    }
}
