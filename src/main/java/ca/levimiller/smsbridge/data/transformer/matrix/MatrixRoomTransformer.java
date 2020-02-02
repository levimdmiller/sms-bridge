package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import java.util.Collections;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class MatrixRoomTransformer {

  private final RoomNameTransformer roomNameTransformer;

  @Inject
  protected MatrixRoomTransformer(
      RoomNameTransformer roomNameTransformer) {
    this.roomNameTransformer = roomNameTransformer;
  }

  /**
   * Transforms the contact numbers to a room.
   *
   * @param chatNumber - chat service identifier
   * @param smsContact - sms contact
   * @return - create room dto
   */
  public CreateRoomRequest transform(ChatUser chatNumber, Contact smsContact) {
    CreateRoomRequest roomRequest = new CreateRoomRequest();
    roomRequest.setPreset("trusted_private_chat");
    roomRequest.setRoomAliasName(roomNameTransformer.transformEncoded(chatNumber, smsContact));
    roomRequest.setName(roomNameTransformer.transformHumanReadable(chatNumber, smsContact));
    roomRequest.setTopic("Sms Conversation");
    roomRequest.setInvite(Collections.singletonList(chatNumber.getOwnerId()));
    roomRequest.setDirect(ChatUserType.USER.equals(chatNumber.getUserType()));
    return roomRequest;
  }
}
