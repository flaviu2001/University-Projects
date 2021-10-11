package web.dto;

import core.domain.Cat;
import core.domain.CatFoodPrimaryKey;
import core.domain.Food;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CatFoodDTO extends BaseDTO<CatFoodPrimaryKey>{
    Cat cat;
    Food food;
}
