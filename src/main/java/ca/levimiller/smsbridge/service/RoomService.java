package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Contact;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RoomService {

  /**
   * Gets the room id for the given recipients.
   *
   * @param chatUser - chat user/room
   * @param smsUser - sms contact (not linked to chat service)
   * @return - room id.
   */
  String getRoom(@Valid @NotNull ChatUser chatUser,
      @Valid @NotNull ChatUser smsUser);

  /**
   * Gets the sms Contact.
   *
   * @param roomId - room id
   * @return - sms contact
   */
  Contact getNumber(@NotNull String roomId);
}
