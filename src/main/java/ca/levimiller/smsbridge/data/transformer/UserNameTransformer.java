package ca.levimiller.smsbridge.data.transformer;

import ca.levimiller.smsbridge.data.model.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNameTransformer {

  /**
   * Transforms the contact to a username.
   * @param smsContact - sms contact
   * @return - contact's username
   */
  default String transform(Contact smsContact) {
    if (smsContact == null) {
      return null;
    }
    return smsContact.getNumber();
  }
}
