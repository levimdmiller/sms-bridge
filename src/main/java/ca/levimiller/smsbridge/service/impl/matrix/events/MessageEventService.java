package ca.levimiller.smsbridge.service.impl.matrix.events;

import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomMessageTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.error.TransformationException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.MatrixEventService;
import ca.levimiller.smsbridge.service.MessageService;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageEventService implements
    MatrixEventService<RoomMessage<RoomMessageContent>, RoomMessageContent> {
  private final MatrixRoomMessageTransformer roomMessageTransformer;
  private final ChatService twilioChatService;
  private final MessageService messageService;

  @Inject
  public MessageEventService(
      MatrixRoomMessageTransformer roomMessageTransformer,
      @Qualifier("twilioChatService") ChatService twilioChatService,
      MessageService messageService) {
    this.roomMessageTransformer = roomMessageTransformer;
    this.twilioChatService = twilioChatService;
    this.messageService = messageService;
  }

  @Override
  public void process(RoomMessage<RoomMessageContent> event) {
    log.debug("Received matrix event: {}", event);
    try {
      Message message = roomMessageTransformer.transform(event);
      messageService.save(message);
      twilioChatService.sendMessage(message);
    } catch (TransformationException e) {
      throw new BadRequestException("Failed to parse message event: ", e);
    }
  }

  @Override
  public String getType() {
    return "m.room.message";
  }
}
