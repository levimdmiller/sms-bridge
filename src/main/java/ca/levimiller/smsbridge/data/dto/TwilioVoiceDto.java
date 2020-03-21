package ca.levimiller.smsbridge.data.dto;

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
public class TwilioVoiceDto extends TwilioBaseDto {

  @NotNull
  String callSid;
  @NotNull
  String direction;

  String callStatus;
}
