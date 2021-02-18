package ca.levimiller.smsbridge.service.impl.matrix.events;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomMessageTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.error.TransformationException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.MatrixEventService;
import ca.levimiller.smsbridge.service.MessageService;
import ca.levimiller.smsbridge.service.OutgoingAttachmentService;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageEventService implements MatrixEventService<RoomMessage<RoomMessageContent>> {

  private final MatrixRoomMessageTransformer roomMessageTransformer;
  private final ChatService twilioChatService;
  private final MessageService messageService;
  private final ChatUserRepository chatUserRepository;
  private final OutgoingAttachmentService attachmentService;

  @Inject
  public MessageEventService(
      MatrixRoomMessageTransformer roomMessageTransformer,
      @Qualifier("twilioChatService") ChatService twilioChatService,
      MessageService messageService,
      ChatUserRepository chatUserRepository,
      OutgoingAttachmentService attachmentService) {
    this.roomMessageTransformer = roomMessageTransformer;
    this.twilioChatService = twilioChatService;
    this.messageService = messageService;
    this.chatUserRepository = chatUserRepository;
    this.attachmentService = attachmentService;
  }

  @Override
  public void process(RoomMessage<RoomMessageContent> event) {
    log.debug("Received matrix event: {}", event);
    try {
      Message message = roomMessageTransformer.transform(event);
      // don't send texts for messages from virtual users.
      if (chatUserRepository.isVirtual(message.getFromContact())) {
        return;
      }

      message.getMedia().forEach(media -> {
        media.setMessage(message);
      });
      messageService.save(message);

      twilioChatService.sendMessage(message);
      message.getMedia().forEach(attachmentService::sendAttachment);
    } catch (TransformationException e) {
      throw new BadRequestException("Failed to parse message event: ", e);
    }
  }

  @Override
  public String getType() {
    return "m.room.message";
  }
}
