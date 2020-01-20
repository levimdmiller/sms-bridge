package ca.levimiller.smsbridge.service.impl.matrix.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomMessageTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.error.TransformationException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.MatrixEventService;
import ca.levimiller.smsbridge.service.MessageService;
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
  private final MatrixEventService<RoomMessage<RoomMessageContent>,
      RoomMessageContent> messageEventService;
  @MockBean
  private MatrixRoomMessageTransformer roomMessageTransformer;
  @MockBean
  @Qualifier("twilioChatService")
  private ChatService twilioChatService;
  @MockBean
  private MessageService messageService;

  private RoomMessage<RoomMessageContent> roomMessage;

  @Autowired
  MessageEventServiceTest(MatrixEventService<RoomMessage<RoomMessageContent>,
      RoomMessageContent> messageEventService) {
    this.messageEventService = messageEventService;
  }

  @BeforeEach
  void setUp() {
    roomMessage = new RoomMessage<>();
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
    Message message = new Message();
    when(roomMessageTransformer.transform(roomMessage)).thenReturn(message);
    messageEventService.process(roomMessage);
    verify(messageService).save(message);
    verify(twilioChatService).sendMessage(message);
  }

  @Test
  void getType() {
    assertEquals("m.room.message", messageEventService.getType());
  }
}
