package web.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class FollowerDTO implements Serializable {
    private Long id;
    private String name;
    private String birthday;
    private AddressDTO address;
}
