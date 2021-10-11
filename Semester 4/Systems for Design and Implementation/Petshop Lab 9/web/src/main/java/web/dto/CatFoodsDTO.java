package web.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatFoodsDTO {
    Set<CatFoodDTO> catFoods;
}
