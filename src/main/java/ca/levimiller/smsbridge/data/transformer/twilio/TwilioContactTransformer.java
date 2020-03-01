package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.db.ContactRepository;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.ContactTransformer;
import ca.levimiller.smsbridge.data.transformer.PhoneNumberTransformer;
import ca.levimiller.smsbridge.data.transformer.qualifiers.From;
import ca.levimiller.smsbridge.data.transformer.qualifiers.PhoneNumber;
import ca.levimiller.smsbridge.data.transformer.qualifiers.To;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
    uses = {ContactRepository.class, PhoneNumberTransformer.class, LocationTransformer.class})
public abstract class TwilioContactTransformer {
  @Autowired
  private ContactTransformer contactTransformer;

  @To
  public Contact transformTo(TwilioSmsDto dto) {
    return contactTransformer.transform(dto.getTo())
        .orElseGet(() -> this.transformToElse(dto));
  }

  @From
  public Contact transformFrom(TwilioSmsDto dto) {
    return contactTransformer.transform(dto.getFrom())
        .orElseGet(() -> this.transformToElse(dto));
  }

  @Mapping(source = "to", target = "number", qualifiedBy = PhoneNumber.class)
  @Mapping(source = ".", target = "location", qualifiedBy = To.class)
  protected abstract Contact transformToElse(TwilioSmsDto dto);

  @Mapping(source = "from", target = "number", qualifiedBy = PhoneNumber.class)
  @Mapping(source = ".", target = "location", qualifiedBy = From.class)
  protected abstract Contact transformFromElse(TwilioSmsDto dto);
}
