package ca.levimiller.smsbridge.config;

import com.twilio.security.RequestValidator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Getter
@Setter
public class TwilioConfig {

  /**
   * Token used to validate X-Twilio-Signature
   */
  private String token;

  @Bean
  RequestValidator requestValidator() {
    return new RequestValidator(token);
  }
}
