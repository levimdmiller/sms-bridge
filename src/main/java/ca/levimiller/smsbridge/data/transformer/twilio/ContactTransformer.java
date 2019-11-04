package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.From;
import ca.levimiller.smsbridge.data.transformer.To;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationTransformer.class)
public interface ContactTransformer {

  @To
  @Mapping(source = "to", target = "number")
  @Mapping(source = ".", target = "location", qualifiedBy = To.class)
  Contact transformTo(TwilioSmsDto dto);

  @From
  @Mapping(source = "from", target = "number")
  @Mapping(source = ".", target = "location", qualifiedBy = From.class)
  Contact transformFrom(TwilioSmsDto dto);
}
