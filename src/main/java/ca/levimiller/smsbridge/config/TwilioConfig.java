package ca.levimiller.smsbridge.config;

import ca.levimiller.smsbridge.twilio.CapabilityTokenFactory;
import ca.levimiller.smsbridge.twilio.MessageCreatorFactory;
import com.twilio.Twilio;
import com.twilio.jwt.client.ClientCapability;
import com.twilio.jwt.client.IncomingClientScope;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.security.RequestValidator;
import java.util.List;
import javax.annotation.PostConstruct;
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
   * Account SID.
   */
  @NotEmpty
  private String sid;
  /**
   * Token used to validate X-Twilio-Signature.
   */
  @NotEmpty
  private String token;

  private String region = "wss://chunderw-vpc-gll.twilio.com/signal";

  @Bean
  RequestValidator requestValidator() {
    return new RequestValidator(token);
  }

  @PostConstruct
  public void init() {
    Twilio.init(sid, token);
  }

  @Bean
  public MessageCreatorFactory messageCreatorFactory() {
    return Message::creator;
  }

  @Bean
  public CapabilityTokenFactory capabilityTokenFactory() {
    return clientName -> new ClientCapability.Builder(sid, token)
        .scopes(List.of(new IncomingClientScope(clientName)))
        .build()
        .toJwt();
  }
}
