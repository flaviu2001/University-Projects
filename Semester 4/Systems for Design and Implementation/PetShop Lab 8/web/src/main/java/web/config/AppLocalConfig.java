package web.config;

import core.config.JPAConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@ComponentScan({"core"})
@Import({JPAConfig.class})
@PropertySources({@PropertySource(value = "classpath:programData/settings.properties")})
public class AppLocalConfig {
    /**
     * Enables placeholders usage with SpEL expressions.
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
