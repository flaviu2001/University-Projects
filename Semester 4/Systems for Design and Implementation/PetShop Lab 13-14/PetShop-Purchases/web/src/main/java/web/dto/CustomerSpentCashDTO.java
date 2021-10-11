package web.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class CustomerSpentCashDTO implements Serializable {
    Long customerId;
    Integer totalCash;
}
