package server.config;

import common.domain.*;
import common.domain.validators.*;
import common.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import repository.IRepository;
import repository.databaseRepository.*;
import service.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Stream;

@Configuration
public class ServerConfig {
    @Bean
    CatDatabaseRepository getCatDatabaseRepository(){
        return new CatDatabaseRepository(new CatValidator());
    }

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
        rmiServiceExporter.setService(new CatServiceServerImpl(
                getCatDatabaseRepository(),
                getPurchaseRepository(),
                getCatFoodDatabaseRepository()
        ));
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
                getCatDatabaseRepository(),
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
                getCatDatabaseRepository(),
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
