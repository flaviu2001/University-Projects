package web.dto;

import core.domain.Customer;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class CustomerSpentCashDTO implements Serializable {
    Customer customer;
    Integer totalCash;
}
