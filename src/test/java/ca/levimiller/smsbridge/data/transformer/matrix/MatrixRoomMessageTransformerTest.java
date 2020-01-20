package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.error.TransformationException;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import io.github.ma1uta.matrix.event.message.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixRoomMessageTransformerTest {
  private final MatrixRoomMessageTransformer roomMessageTransformer;

  @MockBean
  private MatrixContactTransformer matrixContactTransformer;

  private String roomId;
  private String sender;
  private RoomMessage<RoomMessageContent> roomMessage;
  private Contact to;
  private Contact from;

  @Autowired
  MatrixRoomMessageTransformerTest(
      MatrixRoomMessageTransformer roomMessageTransformer) {
    this.roomMessageTransformer = roomMessageTransformer;
  }

  @BeforeEach
  void setUp() throws TransformationException {
    roomId = "roomId";
    sender = "sender";
    roomMessage = new RoomMessage<>();
    to = Contact.builder().id(1L).build();
    from = Contact.builder().id(2L).build();

    roomMessage.setEventId("eventId");
    Text text = new Text();
    text.setBody("body");
    roomMessage.setContent(text);
    roomMessage.setRoomId(roomId);
    roomMessage.setSender(sender);

    when(matrixContactTransformer.transformTo(roomId)).thenReturn(to);
    when(matrixContactTransformer.transformFrom(sender)).thenReturn(from);
  }

  @Test
  void transform() throws TransformationException {
    Message message = roomMessageTransformer.transform(roomMessage);
    assertEquals("eventId", message.getUid());
    assertEquals("body", message.getBody());
    assertEquals(to, message.getToContact());
    assertEquals(from, message.getFromContact());
  }

  @Test
  void transform_TransformationException() throws TransformationException {
    when(matrixContactTransformer.transformFrom(sender))
        .thenThrow(new TransformationException());

    assertThrows(TransformationException.class,
        () -> roomMessageTransformer.transform(roomMessage));
  }
}
