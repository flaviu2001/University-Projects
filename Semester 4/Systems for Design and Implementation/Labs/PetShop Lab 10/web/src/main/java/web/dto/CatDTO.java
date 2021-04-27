package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CatDTO extends BaseDTO<Long> {
    private String name, breed;
    private Integer catYears;

}
