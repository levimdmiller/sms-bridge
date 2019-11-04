package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TwilioLocationTransformer {

  @Mapping(source = "toCity", target = "city")
  @Mapping(source = "toState", target = "state")
  @Mapping(source = "toCountry", target = "country")
  @Mapping(source = "toZip", target = "zip")
  Location transformTo(TwilioSmsDto dto);

  @Mapping(source = "fromCity", target = "city")
  @Mapping(source = "fromState", target = "state")
  @Mapping(source = "fromCountry", target = "country")
  @Mapping(source = "fromZip", target = "zip")
  Location transformFrom(TwilioSmsDto dto);
}
