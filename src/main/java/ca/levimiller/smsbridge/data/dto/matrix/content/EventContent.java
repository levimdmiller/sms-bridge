package ca.levimiller.smsbridge.data.dto.matrix.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventContent implements Serializable {

  @JsonProperty("msgtype")
  private String type;
  private String body;

  public EventContent(ContentType type, String body) {
    setType(type);
    this.body = body;
  }

  public void setType(ContentType type) {
    this.type = type == null ? null : type.getType();
  }
}
