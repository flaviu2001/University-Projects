package client.config;

import client.service.*;
import client.ui.UI;
import common.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class ClientConfig {
    @Bean
    RmiProxyFactoryBean rmiCatProxyFactoryBean() {

        RmiProxyFactoryBean rmiCatProxyFactoryBean = new RmiProxyFactoryBean();
        rmiCatProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ICatService");
        rmiCatProxyFactoryBean.setServiceInterface(ICatService.class);
        return rmiCatProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiFoodProxyFactoryBean() {
        RmiProxyFactoryBean rmiFoodProxyFactoryBean = new RmiProxyFactoryBean();
        rmiFoodProxyFactoryBean.setServiceUrl("rmi://localhost:1099/IFoodService");
        rmiFoodProxyFactoryBean.setServiceInterface(IFoodService.class);
        return rmiFoodProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiCatFoodProxyFactoryBean() {
        RmiProxyFactoryBean rmiCatFoodProxyFactoryBean = new RmiProxyFactoryBean();
        rmiCatFoodProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ICatFoodService");
        rmiCatFoodProxyFactoryBean.setServiceInterface(ICatFoodService.class);
        return rmiCatFoodProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiCustomerProxyFactoryBean() {
        RmiProxyFactoryBean rmiCustomerProxyFactoryBean = new RmiProxyFactoryBean();
        rmiCustomerProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ICustomerService");
        rmiCustomerProxyFactoryBean.setServiceInterface(ICustomerService.class);
        return rmiCustomerProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rmiPurchaseProxyFactoryBean() {
        RmiProxyFactoryBean rmiPurchaseProxyFactoryBean = new RmiProxyFactoryBean();
        rmiPurchaseProxyFactoryBean.setServiceUrl("rmi://localhost:1099/IPurchaseService");
        rmiPurchaseProxyFactoryBean.setServiceInterface(IPurchaseService.class);
        return rmiPurchaseProxyFactoryBean;
    }

    @Bean
    UI ui() {
        return new UI(
                catServiceClient(),
                foodServiceClient(),
                catFoodServiceClient(),
                customerServiceClient(),
                purchaseServiceClient()
        );
    }

    @Bean
    ICatService catServiceClient() {
        return new CatServiceClientImpl();
    }

    @Bean
    IFoodService foodServiceClient() {
        return new FoodServiceClientImpl();
    }

    @Bean
    ICustomerService customerServiceClient() {
        return new CustomerServiceClientImpl();
    }

    @Bean
    ICatFoodService catFoodServiceClient() {
        return new CatFoodServiceClientImpl();
    }

    @Bean
    IPurchaseService purchaseServiceClient() {
        return new PurchaseServiceClientImpl();
    }

}
