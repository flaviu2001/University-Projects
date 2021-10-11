package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class AddressDTO {
    private Long id;
    private String city;
    private String street;
}
