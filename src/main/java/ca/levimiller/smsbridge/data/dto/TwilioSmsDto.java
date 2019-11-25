package ca.levimiller.smsbridge.data.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioSmsDto {

  @NotNull
  String messageSid;
  @NotNull
  String accountSid;
  @NotNull
  String from;
  @NotNull
  String to;
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
  String fromCity;
  String fromState;
  String fromZip;
  String fromCountry;
  String toCity;
  String toState;
  String toZip;
  String toCountry;
}
