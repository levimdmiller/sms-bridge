package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Contact;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface UserService {

  /**
   * Gets the room id for the given recipients.
   *
   * @param smsContact - sms contact (not linked to chat service)
   * @return - room id.
   */
  ChatUser getUser(@Valid @NotNull Contact smsContact);
}
