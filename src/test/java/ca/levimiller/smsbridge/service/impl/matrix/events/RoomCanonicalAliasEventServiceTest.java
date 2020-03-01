package ca.levimiller.smsbridge.service.impl.matrix.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.transformer.UserNameTransformer;
import ca.levimiller.smsbridge.matrixsdk.ExtendedAppServiceClient;
import ca.levimiller.smsbridge.service.MatrixEventService;
import ca.levimiller.smsbridge.service.UserService;
import ca.levimiller.smsbridge.util.MockLogger;
import ch.qos.logback.classic.Level;
import io.github.ma1uta.matrix.client.methods.EventMethods;
import io.github.ma1uta.matrix.client.model.event.MembersResponse;
import io.github.ma1uta.matrix.event.RoomMember;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.RoomName;
import io.github.ma1uta.matrix.event.content.RoomNameContent;
import io.github.ma1uta.matrix.event.message.Text;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RoomCanonicalAliasEventServiceTest {

  private final MatrixEventService<RoomName> eventService;

  @MockBean
  private ChatUserRepository chatUserRepository;
  @MockBean
  private UserNameTransformer userNameTransformer;
  @MockBean
  private UserService userService;
  @MockBean
  private ExtendedAppServiceClient matrixClient;
  @Mock
  private EventMethods eventMethods;
  @Mock
  private CompletableFuture<MembersResponse> membersFuture;
  @Mock
  private CompletableFuture<Void> acceptFuture;
  private MockLogger mockLogger;

  private RoomName event;
  private String displayName;

  @Autowired
  RoomCanonicalAliasEventServiceTest(
      MatrixEventService<RoomName> eventService) {
    this.eventService = eventService;
  }

  @BeforeEach
  void setUp() {
    event = new RoomName();
    event.setRoomId("room-id");
    RoomNameContent content = new RoomNameContent();
    content.setName("room-name");
    event.setContent(content);
    displayName = "display-name";

    when(userNameTransformer.transformFromRoomName("room-name")).thenReturn(displayName);
    when(matrixClient.event()).thenReturn(eventMethods);
    when(eventMethods.members("room-id")).thenReturn(membersFuture);
    when(membersFuture.thenAccept(any())).thenReturn(acceptFuture);

    mockLogger = new MockLogger(RoomCanonicalAliasEventService.class);
  }

  @AfterEach
  void tearDown() {
    mockLogger.teardown();
  }

  @Test
  void process_Exception() {
    eventService.process(event);

    // call exceptionally and verify throwable was logged.
    ArgumentCaptor<Function<Throwable, Void>> argumentCaptor = ArgumentCaptor
        .forClass(Function.class);
    verify(acceptFuture).exceptionally(argumentCaptor.capture());
    Throwable t = new Throwable();
    argumentCaptor.getValue().apply(t);
    mockLogger.verify(Level.ERROR, "Failed to rename virtual users in room: ", t);
  }

  @Test
  void process_Success() {
    eventService.process(event);

    // set up test data:
    RoomMessage<Text> messageEvent = new RoomMessage<>();
    RoomMember noVirtualUsers = new RoomMember();
    noVirtualUsers.setSender("noVirtualUsers-sender");
    RoomMember hasVirtualUsers = new RoomMember();
    hasVirtualUsers.setSender("hasVirtualUsers-sender");
    MembersResponse response = new MembersResponse();
    response.setChunk(List.of(
        messageEvent, noVirtualUsers, hasVirtualUsers
    ));

    when(chatUserRepository
        .existsByOwnerIdAndUserType("noVirtualUsers-sender", ChatUserType.VIRTUAL_USER))
        .thenReturn(false);
    when(chatUserRepository
        .existsByOwnerIdAndUserType("hasVirtualUsers-sender", ChatUserType.VIRTUAL_USER))
        .thenReturn(true);

    // mock call thenAccept
    ArgumentCaptor<Consumer<MembersResponse>> argumentCaptor = ArgumentCaptor
        .forClass(Consumer.class);
    verify(membersFuture).thenAccept(argumentCaptor.capture());
    argumentCaptor.getValue().accept(response);

    // verifications (RoomMessage would throw exception)
    verify(userService).renameUser("hasVirtualUsers-sender", displayName);
    verify(userService, times(0))
        .renameUser(eq("noVirtualUsers-sender"), any());
  }

  @Test
  void testType() {
    assertEquals(RoomName.TYPE, eventService.getType());
  }
}
