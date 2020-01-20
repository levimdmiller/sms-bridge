package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RoomService {

  /**
   * Gets the room id for the given recipients.
   *
   * @param chatNumber - chat user/room
   * @param smsContact - sms contact (not linked to chat service)
   * @return - room id.
   */
  String getRoom(@Valid @NotNull NumberRegistration chatNumber,
      @Valid @NotNull Contact smsContact);

  /**
   * Gets the sms Contact.
   *
   * @param roomId - room id
   * @return - sms contact
   */
  Contact getNumber(@NotNull String roomId);
}
