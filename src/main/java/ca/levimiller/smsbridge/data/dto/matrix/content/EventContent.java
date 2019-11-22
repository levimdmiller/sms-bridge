package ca.levimiller.smsbridge.data.dto.matrix.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventContent implements Serializable {

  @JsonProperty("msgtype")
  private ContentType type;
  private String body;
}
