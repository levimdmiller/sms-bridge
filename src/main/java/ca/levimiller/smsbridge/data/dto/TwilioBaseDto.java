package ca.levimiller.smsbridge.data.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioBaseDto {
  @NotNull
  String accountSid;
  @NotNull
  String from;
  @NotNull
  String to;

  String fromCity;
  String fromCountry;
  String fromState;
  String fromZip;

  String toCity;
  String toCountry;
  String toState;
  String toZip;
}
