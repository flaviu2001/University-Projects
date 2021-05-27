package web.converter;

import core.domain.Customer;
import org.springframework.stereotype.Component;
import web.dto.CustomerDTO;

@Component
public class CustomerConverter extends BaseConverter<Long, Customer,  CustomerDTO> {

    @Override
    public Customer convertDtoToModel(CustomerDTO dto) {
        var customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customer;
    }

    @Override
    public CustomerDTO convertModelToDto(Customer customer) {
        var customerDTO = new CustomerDTO(customer.getName(), customer.getPhoneNumber());
        customerDTO.setId(customer.getId());
        return customerDTO;
    }
}
