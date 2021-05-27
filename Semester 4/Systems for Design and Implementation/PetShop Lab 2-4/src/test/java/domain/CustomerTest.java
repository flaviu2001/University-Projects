package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    private static final Long customerId = 1L;
    private static final Long newCustomerId = 2L;
    private static final String customerName = "c1";
    private static final String newCustomerName = "c2";
    private static final String customerPhoneNumber = "0712345678";
    private static final String newCustomerPhoneNumber = "0733345678";

    private Customer customer;

    @BeforeEach
    public void setUp(){
        customer = new Customer(customerId, customerName, customerPhoneNumber);
    }

    @AfterEach
    public void tearDown(){
        customer = null;
    }

    @Test
    public void testGetName(){
        Assertions.assertEquals(customer.getName(), customerName);
    }

    @Test
    public void testSetName(){
        customer.setName(newCustomerName);
        Assertions.assertEquals(customer.getName(), newCustomerName);
    }

    @Test
    public void testGetPhoneNumber(){
        Assertions.assertEquals(customer.getPhoneNumber(), customerPhoneNumber);
    }

    @Test
    public void testSetPhoneNumber(){
        customer.setPhoneNumber(newCustomerPhoneNumber);
        Assertions.assertEquals(customer.getPhoneNumber(), newCustomerPhoneNumber);
    }

    @Test
    public void testEqualForDifferentCustomersReturnsFalse(){
        Customer newCustomer = new Customer(newCustomerId, customerName, customerPhoneNumber);
        Assertions.assertNotEquals(customer, newCustomer);
    }

    @Test
    public void testEqualForSameCustomerReturnsTrue(){
        Customer newCustomer = new Customer(customerId, customerName, customerPhoneNumber);
        Assertions.assertEquals(customer, newCustomer);
    }
}
