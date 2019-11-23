package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.dto.matrix.EventDto;
import java.util.UUID;
import javax.inject.Inject;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MatrixEventService {

  private final RestTemplate restTemplate;

  @Inject
  public MatrixEventService(@Qualifier("matrixTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Sends the given event to the room specified in the event dto
   * @param event - event to send
   */
  public void sendRoomEvent(EventDto event) {
    sendWithRetry(event, UUID.randomUUID());
  }

  @Retryable(
      value = HttpStatusCodeException.class,
      backoff = @Backoff(
          delay = 2000, // 2 seconds
          multiplier = 8, // back off very quickly, as server is probably down
          maxDelay = 172800000 // 2 days
      )
  )
  private void sendWithRetry(EventDto event, UUID transactionId) {
    String uri = StringUtils.isEmpty(event.getStateKey())
        ? getStateUri(event)
        : getMessageUri(event, transactionId);

    restTemplate.put(uri, event);
  }

  private String getMessageUri(EventDto event, UUID transactionId) {
    UriComponentsBuilder builder = UriComponentsBuilder
        .fromHttpUrl("/rooms/{roomId}/send/{eventType}/{txnId}");
    return builder.buildAndExpand(
        event.getRoomId(),
        event.getType().getCode(),
        transactionId.toString()
    ).toUriString();
  }

  private String getStateUri(EventDto event) {
    UriComponentsBuilder builder = UriComponentsBuilder
        .fromHttpUrl("/rooms/{roomId}/state/{eventType}/{stateKey}");
    return builder.buildAndExpand(
        event.getRoomId(),
        event.getType().getCode(),
        event.getStateKey()
    ).toUriString();
  }
}
