package web.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class SimplePurchasePrimaryKeyDTO implements Serializable {
    Long catId, customerId;
}
