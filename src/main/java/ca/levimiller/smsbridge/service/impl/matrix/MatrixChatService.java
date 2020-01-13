package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.db.NumberRegistryRepository;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.RoomService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatrixChatService implements ChatService {

  private final NumberRegistryRepository numberRegistryRepository;
  private final RoomService roomService;
  private final AppServiceClient matrixClient;

  @Inject
  public MatrixChatService(
      NumberRegistryRepository numberRegistryRepository,
      RoomService roomService, AppServiceClient matrixClient) {
    this.numberRegistryRepository = numberRegistryRepository;
    this.roomService = roomService;
    this.matrixClient = matrixClient;
  }

  @Override
  public void sendMessage(Message message) {
    // Find channel or room registered to the destination of the message
    NumberRegistration to = numberRegistryRepository.findDistinctByContact(message.getToContact())
        .orElseThrow(() -> {
          log.error("Destination number is not registered: {}", message.getToContact());
          return new NotFoundException("Destination number is not registered.");
        });

    // Get room id (ensure created/joined/etc.)
    String roomId = roomService.getRoom(to, message.getFromContact());
    matrixClient.event().sendMessage(roomId, message.getBody());
  }
}
