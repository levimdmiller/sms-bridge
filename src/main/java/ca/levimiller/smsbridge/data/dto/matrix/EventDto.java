package ca.levimiller.smsbridge.data.dto.matrix;

import ca.levimiller.smsbridge.data.dto.matrix.content.EventContent;
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
  private EventContent content;
  private String type;
  private String sender;

  // Optional information about the event
  @JsonProperty("origin_server_ts")
  private Long originServerTs;

  // State Event Fields. Key tuple (type, state_key)
  @JsonProperty("state_key")
  private String stateKey;

  public void setType(EventType type) {
    this.type = type == null ? null : type.getCode();
  }

  public void setType(String type) {
    this.type = type;
  }

  public static class EventDtoBuilder {
    public EventDtoBuilder type(EventType type) {
      this.type = type == null ? null : type.getCode();
      return this;
    }
  }
}
