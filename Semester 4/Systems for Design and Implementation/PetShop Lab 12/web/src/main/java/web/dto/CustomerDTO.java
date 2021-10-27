package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerDTO extends BaseDTO<Long>{
    private PetHouseDTO petHouseDTO;
    private String name;
    private String phoneNumber;
}
