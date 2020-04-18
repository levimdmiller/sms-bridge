package ca.levimiller.smsbridge.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioWsMessage {

  public enum Version {
    V1_5("1.5");

    @Getter private String type;

    Version(String type) {
      this.type = type;
    }
  }

  String type;
  Version version;
  Object payload;
}
