package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class CatFoodPrimaryKeyDTO{
    Long catId, foodId;
}
