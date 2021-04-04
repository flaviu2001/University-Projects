package server.config;

import common.domain.validators.CatFoodValidator;
import common.domain.validators.CustomerValidator;
import common.domain.validators.FoodValidator;
import common.domain.validators.PurchaseValidator;
import common.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import repository.databaseRepository.CatFoodDatabaseRepository;
import repository.databaseRepository.CustomerDatabaseRepository;
import repository.databaseRepository.FoodDatabaseRepository;
import repository.databaseRepository.PurchaseDatabaseRepository;
import service.CatFoodServiceServerImpl;
import service.CustomerServiceServerImpl;
import service.FoodServiceServerImpl;
import service.PurchaseServiceServerImpl;

@Configuration
@ComponentScan({"repository", "service"})
public class ServerConfig {
    @Autowired
    private ApplicationContext context;

    @Bean
    FoodDatabaseRepository getFoodDatabaseRepository() {
        return new FoodDatabaseRepository(new FoodValidator());
    }

    @Bean
    CustomerDatabaseRepository getCustomerDatabaseRepository(){
        return new CustomerDatabaseRepository(new CustomerValidator());
    }

    @Bean
    CatFoodDatabaseRepository getCatFoodDatabaseRepository(){
        return new CatFoodDatabaseRepository(new CatFoodValidator());
    }

    @Bean
    PurchaseDatabaseRepository getPurchaseRepository(){
        return new PurchaseDatabaseRepository(new PurchaseValidator());
    }

    @Bean
    RmiServiceExporter rmiCatServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICatService.class);
        rmiServiceExporter.setService(context.getBean(ICatService.class));
        rmiServiceExporter.setServiceName("ICatService");
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter rmiFoodServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(IFoodService.class);
        rmiServiceExporter.setService(new FoodServiceServerImpl(
                getFoodDatabaseRepository(),
                getCatFoodDatabaseRepository()
        ));
        rmiServiceExporter.setServiceName("IFoodService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiCatFoodServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICatFoodService.class);
        rmiServiceExporter.setService(new CatFoodServiceServerImpl(
                getCatFoodDatabaseRepository(),
                getFoodDatabaseRepository()
        ));
        rmiServiceExporter.setServiceName("ICatFoodService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiPurchaseServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(IPurchaseService.class);
        rmiServiceExporter.setService(new PurchaseServiceServerImpl(
                getPurchaseRepository(),
                getCustomerDatabaseRepository()
        ));
        rmiServiceExporter.setServiceName("IPurchaseService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiCustomerServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICustomerService.class);
        rmiServiceExporter.setService(new CustomerServiceServerImpl(
                getCustomerDatabaseRepository()
        ));
        rmiServiceExporter.setServiceName("ICustomerService");
        return rmiServiceExporter;
    }
}
