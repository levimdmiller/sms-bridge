package ca.levimiller.smsbridge.service.impl.twilio;

import ca.levimiller.smsbridge.config.TwilioConfig;
import ca.levimiller.smsbridge.data.dto.TwilioTokenDto;
import ca.levimiller.smsbridge.data.dto.TwilioWsMessage;
import ca.levimiller.smsbridge.data.dto.TwilioWsMessage.Version;
import ca.levimiller.smsbridge.error.WebSocketException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class TwilioWebsocketClient extends WebSocketClient {
  private final ObjectMapper objectMapper;
  private final String token;

  public TwilioWebsocketClient(
      TwilioConfig twilioConfig,
      ObjectMapper objectMapper,
      String token
  ) throws URISyntaxException {
    super(new URI(twilioConfig.getRegion()));
    this.objectMapper = objectMapper;
    this.token = token;
  }

  @Override
  public void onOpen(ServerHandshake serverHandshake) {
    log.info("open: {}", serverHandshake);
    send("listen", TwilioTokenDto.builder()
        .token(token)
        .build());
  }

  @Override
  public void onMessage(String s) {
    log.info("message {}", s);
  }

  @Override
  public void onClose(int i, String s, boolean b) {
    log.info("closed: {}, {}, {}", i, s, b);
  }

  @Override
  public void onError(Exception e) {
    log.error("Twilio websocket error:", e);
  }

  public void send(String type, Object text) throws WebSocketException {
    try {
      super.send(objectMapper.writeValueAsString(
          TwilioWsMessage.builder()
              .type(type)
              .version(Version.V1_5)
              .payload(text)
              .build()
      ));
    } catch (JsonProcessingException e) {
      log.error("Error serializing message: ", e);
      throw new WebSocketException("Error serializing message: ", e);
    }
  }
}
