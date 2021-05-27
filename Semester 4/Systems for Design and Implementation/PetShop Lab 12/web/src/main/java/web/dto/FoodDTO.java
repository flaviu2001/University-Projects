package web.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FoodDTO extends BaseDTO<Long>{
    private String name, producer;
    private Date expirationDate;
}
