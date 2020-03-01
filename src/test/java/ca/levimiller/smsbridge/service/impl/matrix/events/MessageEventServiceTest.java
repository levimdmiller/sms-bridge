package ca.levimiller.smsbridge.service.impl.matrix.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomMessageTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.error.TransformationException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MessageEventServiceTest {
  private final MatrixEventService<RoomMessage<RoomMessageContent>> messageEventService;
  @MockBean
  private MatrixRoomMessageTransformer roomMessageTransformer;
  @MockBean
  @Qualifier("twilioChatService")
  private ChatService twilioChatService;
  @MockBean
  private MessageRepository messageRepository;
  @MockBean
  private ChatUserRepository chatUserRepository;

  private RoomMessage<RoomMessageContent> roomMessage;
  private Message message;
  private Contact fromContact;

  @Autowired
  MessageEventServiceTest(MatrixEventService<RoomMessage<RoomMessageContent>> messageEventService) {
    this.messageEventService = messageEventService;
  }

  @BeforeEach
  void setUp() {
    roomMessage = new RoomMessage<>();
    message = new Message();
    message.setFromContact(fromContact = new Contact());

    when(chatUserRepository.isVirtual(fromContact)).thenReturn(false);
  }

  @Test
  void process_TransformationException() throws TransformationException {
    TransformationException transformationException = new TransformationException();
    when(roomMessageTransformer.transform(roomMessage)).thenThrow(transformationException);
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> messageEventService.process(roomMessage));
    assertEquals("Failed to parse message event: ", thrown.getMessage());
    assertEquals(transformationException, thrown.getCause());
  }

  @Test
  void process_Success() throws TransformationException {
    when(roomMessageTransformer.transform(roomMessage)).thenReturn(message);
    messageEventService.process(roomMessage);
    verify(messageRepository).save(message);
    verify(twilioChatService).sendMessage(message);
  }

  @Test
  void process_VirtualUser() throws TransformationException {
    when(chatUserRepository.isVirtual(fromContact)).thenReturn(true);
    when(roomMessageTransformer.transform(roomMessage)).thenReturn(message);
    messageEventService.process(roomMessage);
    verify(messageRepository, times(0)).save(any());
    verify(twilioChatService, times(0)).sendMessage(any());
  }

  @Test
  void getType() {
    assertEquals("m.room.message", messageEventService.getType());
  }
}
