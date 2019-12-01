package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;

public interface RoomService {

  /**
   * Gets the room id for the given recipients.
   *
   * @param chatNumber - chat user/room
   * @param smsContact - sms contact
   * @return - room id.
   */
  String getRoom(NumberRegistration chatNumber, Contact smsContact);
}
