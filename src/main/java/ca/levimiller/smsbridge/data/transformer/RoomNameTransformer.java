package ca.levimiller.smsbridge.data.transformer;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.qualifiers.Encoded;
import ca.levimiller.smsbridge.data.transformer.qualifiers.HumanReadable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomNameTransformer {
  String PREFIX = "SMS ";

  /**
   * Returns a human readable name. E.g., SMS +1234567890
   *
   * @param chatNumber - registered id for chat server
   * @param smsContact - sms contact
   * @return - Human readable name
   */
  @HumanReadable
  default String transformHumanReadable(ChatUser chatNumber, Contact smsContact) {
    return PREFIX + smsContact.getNumber();
  }

  /**
   * Returns a room name safe for use int a path/url/identifier. I.e., the name will only have the
   * following characters: [0-9a-zA-Z-] E.g., sms-1234567890
   *
   * @param chatNumber - registered id for chat server
   * @param smsContact - sms contact
   * @return - generated room name
   */
  @Encoded
  default String transformEncoded(ChatUser chatNumber, Contact smsContact) {
    return transformNumber(smsContact.getNumber().replaceAll("\\+", ""));
  }

  default String transformNumber(String number) {
    return "sms-" + number;
  }
}
