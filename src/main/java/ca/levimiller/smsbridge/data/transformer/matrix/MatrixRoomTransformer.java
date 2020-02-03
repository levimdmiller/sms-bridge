package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import java.util.List;
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
   * @param chatUser - chat service identifier
   * @param smsUser - sms contact
   * @return - create room dto
   */
  public CreateRoomRequest transform(ChatUser chatUser, ChatUser smsUser) {
    CreateRoomRequest roomRequest = new CreateRoomRequest();
    roomRequest.setPreset("trusted_private_chat");
    roomRequest.setRoomAliasName(roomNameTransformer.transformEncoded(chatUser,
        smsUser.getContact()));
    roomRequest.setName(roomNameTransformer.transformHumanReadable(chatUser, smsUser.getContact()));
    roomRequest.setTopic("Sms Conversation");
    roomRequest.setInvite(List.of(chatUser.getOwnerId(), smsUser.getOwnerId()));
    roomRequest.setDirect(ChatUserType.USER.equals(chatUser.getUserType()));
    return roomRequest;
  }
}
