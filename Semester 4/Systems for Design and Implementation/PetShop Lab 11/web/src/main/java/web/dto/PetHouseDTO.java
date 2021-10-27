package web.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString()
public class PetHouseDTO implements Serializable {
    private String color;
    private Integer size;
}
