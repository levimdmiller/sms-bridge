package ca.levimiller.smsbridge.data.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class TwilioSmsDto extends TwilioBaseDto {

  @NotNull
  String messageSid;
  @NotNull
  String body;
  @NotNull
  Integer numSegments;
  @NotNull
  Integer numMedia;
  String messagingServiceSid;

  // Optional
  List<String> mediaContentTypes;
  List<String> mediaUrls;
}
