package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.service.UserService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("matrixChatService")
@Slf4j
public class MatrixChatService implements ChatService {

  private final ChatUserRepository chatUserRepository;
  private final RoomService roomService;
  private final UserService userService;
  private final AppServiceClient matrixClient;

  @Inject
  public MatrixChatService(
      ChatUserRepository chatUserRepository,
      RoomService roomService, UserService userService,
      AppServiceClient matrixClient) {
    this.chatUserRepository = chatUserRepository;
    this.roomService = roomService;
    this.userService = userService;
    this.matrixClient = matrixClient;
  }

  @Override
  public void sendMessage(Message message) {
    // Find channel or room registered to the destination of the message
    ChatUser to = chatUserRepository.findDistinctByContact(message.getToContact())
        .orElseThrow(() -> {
          log.error("Destination number is not registered: {}", message.getToContact());
          return new NotFoundException("Destination number is not registered.");
        });

    // Get room id and user id (ensure created/joined/etc.)
    ChatUser from = userService.getUser(message.getFromContact());
    String roomId = roomService.getRoom(to, from);
    matrixClient.userId(from.getOwnerId()).event().sendMessage(roomId, message.getBody());
  }
}
