package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToyDTO extends BaseDTO<Long> {
    long catId;
    String name;
    int price;
}
