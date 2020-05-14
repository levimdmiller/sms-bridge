package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Location;
import ca.levimiller.smsbridge.data.transformer.qualifiers.From;
import ca.levimiller.smsbridge.data.transformer.qualifiers.To;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LocationTransformer {

  @To
  @Mapping(source = "toCity", target = "city")
  @Mapping(source = "toState", target = "state")
  @Mapping(source = "toCountry", target = "country")
  @Mapping(source = "toZip", target = "zip", qualifiedByName =  "zipType")
  Location transformTo(TwilioSmsDto dto);

  @From
  @Mapping(source = "fromCity", target = "city")
  @Mapping(source = "fromState", target = "state")
  @Mapping(source = "fromCountry", target = "country")
  @Mapping(source = "fromZip", target = "zip", qualifiedByName =  "zipType")
  Location transformFrom(TwilioSmsDto dto);

  @Named("zipType")
  default String zip(String zip) {
    return zip == null ? null : zip.replaceAll("\\s+","");
  }
}
