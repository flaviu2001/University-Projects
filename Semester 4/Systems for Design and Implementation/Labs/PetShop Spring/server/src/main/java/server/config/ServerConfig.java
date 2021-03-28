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
    private HashMap<String, String> readSettingsFile() { // this should be removed at the end
        HashMap<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        String configFile = "data/programData/settings.properties";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(configFile);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return propertiesMap;
        }
        Stream.ofNullable(fileInputStream).findAny().ifPresentOrElse((el) ->  {
            try {
                properties.load(fileInputStream);
                propertiesMap.put("database", properties.getProperty("database"));
                propertiesMap.put("user", properties.getProperty("user"));
                propertiesMap.put("password", properties.getProperty("password"));
            } catch (IOException ioException) {
                System.out.println("IOException: " + ioException.getMessage());
            }
        }, () -> System.out.println("Invalid config file"));

        return propertiesMap;
    }

    HashMap<String, String> configurations = readSettingsFile();
    final IRepository<Pair<Long, Long>, CatFood> catFoodRepository = new CatFoodDatabaseRepository(new CatFoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password"));
    final IRepository<Long, Customer> customerRepository = new CustomerDatabaseRepository(new CustomerValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password"));
    final IRepository<Pair<Long, Long>, Purchase> purchaseRepository = new PurchaseDatabaseRepository(new PurchaseValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password"));
    // ^ these too ^

    @Bean
    CatDatabaseRepository getCatDatabaseRepository(){
        return new CatDatabaseRepository(new CatValidator());
    }

    @Bean
    FoodDatabaseRepository getFoodDatabaseRepository() {
        return new FoodDatabaseRepository(new FoodValidator());
    }

    @Bean
    RmiServiceExporter rmiCatServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICatService.class);
        rmiServiceExporter.setService(new CatServiceServerImpl(
                getCatDatabaseRepository(),
                purchaseRepository,
                catFoodRepository
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
                catFoodRepository
        ));
        rmiServiceExporter.setServiceName("IFoodService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiCatFoodServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICatFoodService.class);
        rmiServiceExporter.setService(new CatFoodServiceServerImpl(
                catFoodRepository,
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
                purchaseRepository,
                getCatDatabaseRepository(),
                customerRepository
        ));
        rmiServiceExporter.setServiceName("IPurchaseService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiCustomerServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICustomerService.class);
        rmiServiceExporter.setService(new CustomerServiceServerImpl(
                customerRepository
        ));
        rmiServiceExporter.setServiceName("ICustomerService");
        return rmiServiceExporter;
    }
}
