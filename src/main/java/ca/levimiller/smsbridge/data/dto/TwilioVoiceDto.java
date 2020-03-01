package ca.levimiller.smsbridge.data.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioVoiceDto {

  @NotNull
  String accountSid;
  @NotNull
  String callSid;
  @NotNull
  String from;
  @NotNull
  String to;
  @NotNull
  String direction;

  String callStatus;

  String fromCity;
  String fromCountry;
  String fromState;
  String fromZip;

  String toCity;
  String toCountry;
  String toState;
  String toZip;
}
