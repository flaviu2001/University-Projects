package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class SimplePurchasePrimaryKeyDTO {
    Long catId, customerId;
}
