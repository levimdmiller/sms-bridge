package ca.levimiller.smsbridge.service.impl.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.PhoneNumberTransformer;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.matrixsdk.ExtendedAppServiceClient;
import ca.levimiller.smsbridge.matrixsdk.ExtendedRoomMethods;
import ca.levimiller.smsbridge.matrixsdk.SimpleInviteRequest;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.util.MatrixUtil;
import ca.levimiller.smsbridge.util.MockLogger;
import ch.qos.logback.classic.Level;
import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.methods.EventMethods;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import io.github.ma1uta.matrix.client.model.room.RoomId;
import io.github.ma1uta.matrix.event.RoomCanonicalAlias;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.content.RoomCanonicalAliasContent;
import io.github.ma1uta.matrix.event.message.Text;
import io.github.ma1uta.matrix.impl.exception.MatrixException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@SpringBootTest
class MatrixRoomServiceTest {

  private final RoomService roomService;

  @MockBean
  private MatrixRoomTransformer roomTransformer;
  @MockBean
  private ExtendedAppServiceClient matrixClient;
  @MockBean
  private MatrixUtil matrixUtil;
  @MockBean
  private PhoneNumberTransformer phoneNumberTransformer;
  @Mock
  private ExtendedRoomMethods roomMethods;
  @Mock
  private AppServiceClient userClient;
  @Mock
  private EventMethods eventMethods;
  @Mock
  private CompletableFuture<EventContent> eventFuture;
  @Mock
  private CompletableFuture<RoomId> roomFuture;
  @Mock
  private CompletableFuture<EmptyResponse> inviteFuture;
  @Mock
  private CompletableFuture<RoomId> joinFuture;
  private MockLogger mockLogger;

  private ChatUser chatUser;
  private ChatUser smsUser;
  private CreateRoomRequest createRoomRequest;
  private SimpleInviteRequest inviteRequest;
  private RoomId roomId;

  @Autowired
  MatrixRoomServiceTest(RoomService roomService) {
    this.roomService = roomService;
  }

  @BeforeEach
  void setUp() {
    chatUser = ChatUser.builder()
        .ownerId("ownerId")
        .userType(ChatUserType.USER)
        .contact(Contact.builder()
            .number("+registrationNumber")
            .build())
        .build();
    smsUser = ChatUser.builder()
        .ownerId("smsOwnerId")
        .userType(ChatUserType.VIRTUAL_USER)
        .contact(Contact.builder()
            .number("+smsNumber")
            .build())
        .build();
    createRoomRequest = new CreateRoomRequest();
    createRoomRequest.setRoomAliasName("alias");
    roomId = new RoomId();
    roomId.setRoomId("roomId");
    inviteRequest = SimpleInviteRequest.builder()
        .userId(smsUser.getOwnerId())
        .build();

    when(roomTransformer.transform(chatUser, smsUser)).thenReturn(createRoomRequest);
    when(matrixClient.room()).thenReturn(roomMethods);
    when(roomMethods.resolveAlias("#alias:domain.ca"))
        .thenReturn(roomFuture);
    when(roomMethods.create(createRoomRequest))
        .thenReturn(roomFuture);

    when(matrixClient.event()).thenReturn(eventMethods);
    when(eventMethods.eventContent(roomId.getRoomId(), RoomCanonicalAlias.TYPE, ""))
        .thenReturn(eventFuture);

    when(matrixClient.userId("smsOwnerId")).thenReturn(userClient);
    when(userClient.room()).thenReturn(roomMethods);
    when(roomMethods.joinByIdOrAlias(roomId.getRoomId())).thenReturn(joinFuture);
    when(roomMethods.invite(roomId.getRoomId(), inviteRequest)).thenReturn(inviteFuture);
    mockLogger = new MockLogger(MatrixRoomService.class);
  }

  @AfterEach
  void tearDown() {
    mockLogger.teardown();
  }

  @Test
  void getRoom_NumberExists() {
    when(roomFuture.join()).thenReturn(roomId);
    String response = roomService.getRoom(chatUser, smsUser);
    assertEquals("roomId", response);
    verify(roomMethods, times(0)).create(any());
    verifyJoinRoom();
  }

  @Test
  void getRoom_NumberNotFound() {
    // lookup throws 404
    CompletionException e = new CompletionException(new MatrixException("", "", 404));
    when(roomFuture.join())
        .thenThrow(e);
    when(matrixUtil.causedBy(e, HttpStatus.NOT_FOUND)).thenReturn(true);
    // creation mocking
    CompletableFuture<RoomId> roomFuture2 = mock(CompletableFuture.class);
    when(roomMethods.create(createRoomRequest))
        .thenReturn(roomFuture2);
    when(roomFuture2.join()).thenReturn(roomId);

    String response = roomService.getRoom(chatUser, smsUser);
    assertEquals("roomId", response);
    verifyJoinRoom();
  }

  @Test
  void getRoom_NumberNotFoundOrCreated_Exception() {
    when(roomFuture.join())
        .thenThrow(new CompletionException(new MatrixException("", "", 500)));
    assertThrows(BadRequestException.class, () -> roomService.getRoom(chatUser, smsUser));
    verify(roomMethods, times(0)).create(any());
  }

  @Test
  void getRoom_NumberNotFoundOrCreated_Cancelled() {
    when(roomFuture.join())
        .thenThrow(new CancellationException());
    assertThrows(BadRequestException.class, () -> roomService.getRoom(chatUser, smsUser));
    verify(roomMethods, times(0)).create(any());
  }

  @Test
  void testGetNumber_Cancelled() {
    when(eventFuture.join()).thenThrow(new CancellationException());
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getNumber(roomId.getRoomId()));
    assertEquals("Unable to resolve canonical alias for roomId: " + roomId.getRoomId(),
        thrown.getMessage());
  }

  @Test
  void testGetNumber_CompletionException() {
    when(eventFuture.join()).thenThrow(new CompletionException(new Exception()));
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getNumber(roomId.getRoomId()));
    assertEquals("Unable to resolve canonical alias for roomId: " + roomId.getRoomId(),
        thrown.getMessage());
  }

  @Test
  void testGetNumber_WrongEventContent() {
    EventContent content = new Text();
    when(eventFuture.join()).thenReturn(content);
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getNumber(roomId.getRoomId()));
    assertEquals("Incorrect event content for alias lookup: " + content,
        thrown.getMessage());
  }

  @Test
  void testGetNumber_Success() {
    RoomCanonicalAliasContent content = new RoomCanonicalAliasContent();
    content.setAlias("alias");
    when(eventFuture.join()).thenReturn(content);
    when(phoneNumberTransformer.transform("alias")).thenReturn("number");

    Contact result = roomService.getNumber(roomId.getRoomId());
    assertEquals(Contact.builder()
        .number("number")
        .build(), result);
  }

  @Test
  void joinRoomError_Cancelled() {
    CancellationException e = new CancellationException();
    when(roomFuture.join()).thenReturn(roomId);
    when(joinFuture.join()).thenThrow(e);
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getRoom(chatUser, smsUser));
    assertEquals("Unable to add virtual user to room: smsOwnerId - " + roomId.getRoomId(),
        thrown.getMessage());
    mockLogger.verify(Level.ERROR, "Unable to add virtual user to room", e);
  }

  @Test
  void joinRoomError_Completed() {
    CompletionException e = new CompletionException(new Exception());
    when(roomFuture.join()).thenReturn(roomId);
    when(joinFuture.join()).thenThrow(e);
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getRoom(chatUser, smsUser));
    assertEquals("Unable to add virtual user to room: smsOwnerId - " + roomId.getRoomId(),
        thrown.getMessage());
    mockLogger.verify(Level.ERROR, "Unable to add virtual user to room", e);
  }



  @Test
  void inviteRoomError_Cancelled() {
    CancellationException e = new CancellationException();
    when(roomFuture.join()).thenReturn(roomId);
    when(inviteFuture.join()).thenThrow(e);
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getRoom(chatUser, smsUser));
    assertEquals("Unable to add virtual user to room: smsOwnerId - " + roomId.getRoomId(),
        thrown.getMessage());
    mockLogger.verify(Level.ERROR, "Unable to add virtual user to room", e);
    verify(joinFuture, times(0)).join();
  }

  @Test
  void inviteRoomError_Completed() {
    CompletionException e = new CompletionException(new Exception());
    when(roomFuture.join()).thenReturn(roomId);
    when(inviteFuture.join()).thenThrow(e);
    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> roomService.getRoom(chatUser, smsUser));
    assertEquals("Unable to add virtual user to room: smsOwnerId - " + roomId.getRoomId(),
        thrown.getMessage());
    mockLogger.verify(Level.ERROR, "Unable to add virtual user to room", e);
    verify(joinFuture, times(0)).join();
  }

  void verifyJoinRoom() {
    verify(inviteFuture).join();
    verify(joinFuture).join();
  }
}
