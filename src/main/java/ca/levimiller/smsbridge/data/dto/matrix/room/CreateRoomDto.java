package ca.levimiller.smsbridge.data.dto.matrix.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * https://matrix.org/docs/spec/client_server/r0.6.0#creation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDto {

  private RoomPreset preset;
  @JsonProperty("room_alias_name")
  private String roomAliasName;
  private String name;
  private String topic;
  private List<String> invite;
  @JsonProperty("is_direct")
  private boolean isDirect;
}
