package web.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class PostDTO implements Serializable {
    private Long id;
    private String title;
    private String content;
    private List<CommentDTO> comments;
}
