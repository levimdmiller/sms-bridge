package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.dto.TwilioVoiceDto;
import ca.levimiller.smsbridge.data.model.VoiceCall;
import ca.levimiller.smsbridge.data.transformer.qualifiers.From;
import ca.levimiller.smsbridge.data.transformer.qualifiers.To;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TwilioContactTransformer.class})
public interface VoiceCallTransformer {

  @Mapping(source = "callSid", target = "uid")
  @Mapping(source = ".", target = "toContact", qualifiedBy = To.class)
  @Mapping(source = ".", target = "fromContact", qualifiedBy = From.class)
  VoiceCall transform(TwilioVoiceDto dto);
}

