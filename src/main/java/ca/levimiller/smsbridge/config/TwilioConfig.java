package ca.levimiller.smsbridge.config;

import com.twilio.security.RequestValidator;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Validated
@Getter
@Setter
public class TwilioConfig {

  /**
   * Token used to validate X-Twilio-Signature.
   */
  @NotEmpty
  private String token;

  @Bean
  RequestValidator requestValidator() {
    return new RequestValidator(token);
  }
}
