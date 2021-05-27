package web.converter;

import core.domain.Customer;
import core.domain.PetHouse;
import org.springframework.stereotype.Component;
import web.dto.CustomerDTO;
import web.dto.PetHouseDTO;

@Component
public class CustomerConverter extends BaseConverter<Long, Customer,  CustomerDTO> {

    @Override
    public Customer convertDtoToModel(CustomerDTO dto) {
        var customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        PetHouse petHouse = null;
        if (dto.getPetHouseDTO() != null)
            petHouse = new PetHouse(dto.getPetHouseDTO().getSize(), dto.getPetHouseDTO().getColor());
        customer.setPetHouse(petHouse);
        return customer;
    }

    @Override
    public CustomerDTO convertModelToDto(Customer customer) {
        PetHouseDTO petHouseDTO = null;
        if (customer.getPetHouse() != null)
            petHouseDTO = new PetHouseDTO(customer.getPetHouse().getColor(), customer.getPetHouse().getSize());
        var customerDTO = new CustomerDTO(petHouseDTO, customer.getName(), customer.getPhoneNumber());
        customerDTO.setId(customer.getId());
        return customerDTO;
    }
}
