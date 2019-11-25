package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.db.NumberRegistryRepository;
import ca.levimiller.smsbridge.data.dto.matrix.EventDto;
import ca.levimiller.smsbridge.data.dto.matrix.EventType;
import ca.levimiller.smsbridge.data.dto.matrix.content.TextContent;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.util.UuidSource;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatrixChatService implements ChatService {

  private final UuidSource uuidSource;
  private final NumberRegistryRepository numberRegistryRepository;
  private final MatrixEventService eventService;
  private final RoomService roomService;

  @Inject
  public MatrixChatService(
      UuidSource uuidSource,
      NumberRegistryRepository numberRegistryRepository,
      MatrixEventService eventService,
      RoomService roomService) {
    this.uuidSource = uuidSource;
    this.numberRegistryRepository = numberRegistryRepository;
    this.eventService = eventService;
    this.roomService = roomService;
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
    // used by the server to ensure idempotency of requests.
    eventService.sendRoomEvent(EventDto.builder()
        .eventId(uuidSource.newUuid().toString())
        .roomId(roomId)
        .type(EventType.ROOM_MESSAGE)
        .content(TextContent.builder()
            .body(message.getBody())
            .build())
        .build());
  }
}
