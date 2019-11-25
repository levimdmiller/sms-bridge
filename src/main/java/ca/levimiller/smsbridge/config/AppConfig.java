package ca.levimiller.smsbridge.config;

import ca.levimiller.smsbridge.util.UuidSource;
import java.util.UUID;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@EnableConfigurationProperties
public class AppConfig {

  @Bean
  public UuidSource uuidSource() {
    return UUID::randomUUID;
  }
}
