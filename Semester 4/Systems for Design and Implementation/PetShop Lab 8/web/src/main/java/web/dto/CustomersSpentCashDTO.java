package web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomersSpentCashDTO {
    List<CustomerSpentCashDTO> customersSpentCash;
}