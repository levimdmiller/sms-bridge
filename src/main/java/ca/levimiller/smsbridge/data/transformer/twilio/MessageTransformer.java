package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.From;
import ca.levimiller.smsbridge.data.transformer.To;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ContactTransformer.class, MediaTransformer.class})
public interface MessageTransformer {

  @Mapping(source = "messageSid", target = "uid")
  @Mapping(source = ".", target = "toContact", qualifiedBy = To.class)
  @Mapping(source = ".", target = "fromContact", qualifiedBy = From.class)
  @Mapping(source = ".", target = "media")
  Message transform(TwilioSmsDto dto);
}
