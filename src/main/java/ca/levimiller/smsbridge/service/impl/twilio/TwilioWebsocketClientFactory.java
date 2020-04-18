package ca.levimiller.smsbridge.service.impl.twilio;

import ca.levimiller.smsbridge.config.TwilioConfig;
import ca.levimiller.smsbridge.error.WebSocketException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URISyntaxException;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class TwilioWebsocketClientFactory {
  private final ObjectMapper objectMapper;
  private final TwilioConfig twilioConfig;

  @Inject
  public TwilioWebsocketClientFactory(
      ObjectMapper objectMapper,
      TwilioConfig twilioConfig) {
    this.objectMapper = objectMapper;
    this.twilioConfig = twilioConfig;
  }

  public TwilioWebsocketClient newClient(String token) {
    try {
      return new TwilioWebsocketClient(twilioConfig, objectMapper, token);
    } catch (URISyntaxException e) {
      throw new WebSocketException("Error creating websocket client, bad url: "
          + twilioConfig.getRegion());
    }
  }
}
