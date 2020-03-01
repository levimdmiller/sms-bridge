package ca.levimiller.smsbridge.matrixsdk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleInviteRequest {
  @JsonProperty("user_id")
  String userId;
}
