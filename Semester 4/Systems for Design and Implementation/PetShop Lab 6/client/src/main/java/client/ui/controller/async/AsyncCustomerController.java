package client.ui.controller.async;

import common.domain.Customer;
import common.exceptions.PetShopException;
import common.service.ICustomerService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AsyncCustomerController {
    ExecutorService executorService;
    private final ICustomerService customerService;

    public AsyncCustomerController(ExecutorService executorService, ICustomerService customerService) {
        this.executorService = executorService;
        this.customerService = customerService;
    }

    public CompletableFuture<Iterable<Customer>> getCustomersFromRepository(){
        return CompletableFuture.supplyAsync(customerService::getCustomersFromRepository, executorService);
    }

    public CompletableFuture<String> addCustomer(String name, String phoneNumber){
        return CompletableFuture.supplyAsync(() -> {
            try {
                customerService.addCustomer(name, phoneNumber);
                return "Customer added";
            }catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteCustomer(Long id){
        return CompletableFuture.supplyAsync(()->{
            try{
                customerService.deleteCustomer(id);
                return "Customer deleted";
            }
            catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> updateCustomer(Long id, String name, String phoneNumber){
        return CompletableFuture.supplyAsync(()->{
            try{
                customerService.updateCustomer(id, name, phoneNumber);
                return "Customer updated";
            }
            catch (PetShopException exception){
                return exception.getMessage();
            }
        }, executorService);
    }
}
