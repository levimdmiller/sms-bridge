package ca.levimiller.smsbridge.data.dto.matrix;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * https://matrix.org/docs/spec/client_server/r0.6.0#events
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

  @JsonProperty("room_id")
  private String roomId;
  @JsonProperty("event_id")
  private String eventId;
  private Long age;
  private Object content;
  private EventType type;
  private String sender;

  // Optional information about the event
  @JsonProperty("origin_server_ts")
  private Long originServerTs;

  // State Event Fields. Key tuple (type, state_key)
  @JsonProperty("state_key")
  private String stateKey;

}
