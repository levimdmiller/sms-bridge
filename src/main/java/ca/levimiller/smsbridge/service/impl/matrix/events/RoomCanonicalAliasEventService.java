package ca.levimiller.smsbridge.service.impl.matrix.events;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.transformer.UserNameTransformer;
import ca.levimiller.smsbridge.service.MatrixEventService;
import ca.levimiller.smsbridge.service.UserService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.RoomMember;
import io.github.ma1uta.matrix.event.RoomName;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RoomCanonicalAliasEventService implements MatrixEventService<RoomName> {

  private final ChatUserRepository chatUserRepository;
  private final UserNameTransformer userNameTransformer;
  private final UserService userService;
  private final AppServiceClient matrixClient;

  @Inject
  public RoomCanonicalAliasEventService(
      ChatUserRepository chatUserRepository,
      UserNameTransformer userNameTransformer,
      UserService userService, AppServiceClient matrixClient) {
    this.chatUserRepository = chatUserRepository;
    this.userNameTransformer = userNameTransformer;
    this.userService = userService;
    this.matrixClient = matrixClient;
  }

  @Override
  public void process(RoomName event) {
    String displayName = userNameTransformer.transformFromRoomName(event.getContent().getName());
    matrixClient.event().members(event.getRoomId())
        .thenAccept(response -> response.getChunk().stream()
            .filter(RoomMember.class::isInstance)
            .map(RoomMember.class::cast)
            .filter(this::virtualUserExists)
            .forEach(roomMember -> userService.renameUser(roomMember.getSender(), displayName)))
        .exceptionally(e -> {
          log.error("Failed to rename virtual users in room: ", e);
          return null;
        });
  }

  @Override
  public String getType() {
    return RoomName.TYPE;
  }

  private boolean virtualUserExists(RoomMember roomMember) {
    return chatUserRepository.existsByOwnerIdAndUserType(
        roomMember.getSender(), ChatUserType.VIRTUAL_USER);
  }
}
