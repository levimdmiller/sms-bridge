package ca.levimiller.smsbridge.data.dto;

import com.twilio.rest.api.v2010.account.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TwilioTokenDto {
  String token;
  String browserInfo;
}
