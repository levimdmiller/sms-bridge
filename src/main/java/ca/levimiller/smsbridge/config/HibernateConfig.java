package ca.levimiller.smsbridge.config;

import org.hibernate.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

  @Bean
  @Autowired
  public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(Interceptor interceptor) {
    return hibernateProperties -> {
      hibernateProperties.put("hibernate.ejb.interceptor", interceptor);
    };
  }
}
