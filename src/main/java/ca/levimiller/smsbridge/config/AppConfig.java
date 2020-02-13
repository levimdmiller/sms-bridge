package ca.levimiller.smsbridge.config;

import ca.levimiller.smsbridge.util.UuidSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ma1uta.matrix.support.jackson.JacksonContextResolver;
import java.util.UUID;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableRetry
@EnableConfigurationProperties
public class AppConfig {

  @Bean
  public UuidSource uuidSource() {
    return UUID::randomUUID;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
    loggingFilter.setIncludeClientInfo(true);
    loggingFilter.setIncludeQueryString(true);
    loggingFilter.setIncludePayload(true);
    return loggingFilter;
  }

  @Bean
  public ObjectMapper mapper() {
    return new JacksonContextResolver().getContext(null);
  }

  @Bean
  public Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }
}
