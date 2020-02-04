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

  /**
   * Strips the SMS prefix from the room name.
   * @param roomName - room name to format.
   * @return - formatted name
   */
  default String transformFromRoomName(String roomName) {
    return roomName.replaceAll("^" + RoomNameTransformer.PREFIX, "");
  }
}
