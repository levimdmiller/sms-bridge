package ca.levimiller.smsbridge.config;

import javax.inject.Inject;
import org.hibernate.Interceptor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

  @Bean
  @Inject
  public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(Interceptor interceptor) {
    return hibernateProperties -> {
      // https://docs.jboss.org/hibernate/stable/entitymanager/reference/en/html/configuration.html
      hibernateProperties.put("hibernate.session_factory.interceptor", interceptor);
    };
  }
}
