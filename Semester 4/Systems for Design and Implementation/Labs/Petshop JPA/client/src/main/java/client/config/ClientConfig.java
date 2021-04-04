package client.config;

import common.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan({"client.ui.controller.async", "client.ui"})
public class ClientConfig {
    public static final Logger logger = LoggerFactory.getLogger(ClientConfig.class);

    @Bean
    RmiProxyFactoryBean rmiCatProxyFactoryBean() {
        logger.trace("rmiCatProxyFactoryBean - method entered");
        RmiProxyFactoryBean rmiCatProxyFactoryBean = new RmiProxyFactoryBean();
        rmiCatProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ICatService");
        rmiCatProxyFactoryBean.setServiceInterface(ICatService.class);
        logger.trace("rmiCatProxyFactoryBean - method exited");
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
    ExecutorService executorService(){
        return Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
    }
}
