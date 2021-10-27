package web.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class AppUserDTO {
    private Long id;
    private String name;
    private String birthday;
    private AddressDTO address;
    private Set<FollowerDTO> followers;
    private Set<PostDTO> posts;
}
