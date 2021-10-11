package server.config;

import common.domain.validators.PurchaseValidator;
import common.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import service.CustomerServiceServerImpl;
import service.PurchaseServiceServerImpl;

@Configuration
@ComponentScan({"repository", "service"})
public class ServerConfig {
    @Autowired
    private ApplicationContext context;

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
        rmiServiceExporter.setService(context.getBean(IFoodService.class));
        rmiServiceExporter.setServiceName("IFoodService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiCatFoodServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICatFoodService.class);
        rmiServiceExporter.setService(context.getBean(ICatFoodService.class));
        rmiServiceExporter.setServiceName("ICatFoodService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiPurchaseServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(IPurchaseService.class);
        rmiServiceExporter.setService(context.getBean(IPurchaseService.class));
        rmiServiceExporter.setServiceName("IPurchaseService");
        return rmiServiceExporter;
    }
    @Bean
    RmiServiceExporter rmiCustomerServiceExporter() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceInterface(ICustomerService.class);
        rmiServiceExporter.setService(context.getBean(ICustomerService.class));
        rmiServiceExporter.setServiceName("ICustomerService");
        return rmiServiceExporter;
    }
}
