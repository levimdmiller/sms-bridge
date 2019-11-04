package ca.levimiller.smsbridge.data.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioSmsDto {

  String messageSid;
  String accountSid;
  String messagingServiceSid;
  String from;
  String to;
  String body;
  Integer numSegments;
  Integer numMedia;

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
