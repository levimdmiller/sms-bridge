package ca.levimiller.smsbridge.service.impl.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.UserNameTransformer;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixUserRegisterTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.matrixsdk.ExtendedAppServiceClient;
import ca.levimiller.smsbridge.service.UserService;
import ca.levimiller.smsbridge.util.MockLogger;
import ch.qos.logback.classic.Level;
import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.methods.AccountMethods;
import io.github.ma1uta.matrix.client.methods.ProfileMethods;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import io.github.ma1uta.matrix.client.model.auth.LoginResponse;
import java.util.Optional;
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

@SpringBootTest
class MatrixUserServiceTest {
  private final UserService userService;
  @MockBean
  private ChatUserRepository userRepository;
  @MockBean
  private MatrixUserRegisterTransformer matrixUserRegisterTransformer;
  @MockBean
  private UserNameTransformer userNameTransformer;
  @MockBean
  private ExtendedAppServiceClient matrixClient;
  @Mock
  private AppServiceClient userClient;
  @Mock
  private AccountMethods accountMethods;
  @Mock
  private ProfileMethods profileMethods;
  @Mock
  private CompletableFuture<LoginResponse> registerFuture;
  @Mock
  private CompletableFuture<EmptyResponse> displayFuture;
  private MockLogger mockLogger;

  private String userId;
  private String displayName;
  private Contact contact;
  private ChatUser chatUser;
  private RegisterRequest registerRequest;
  private LoginResponse loginResponse;

  @Autowired
  MatrixUserServiceTest(UserService userService) {
    this.userService = userService;
  }

  @BeforeEach
  void setUp() {
    userId = "user-id";
    displayName = "display-name";
    contact = new Contact();
    chatUser = new ChatUser();
    registerRequest = new RegisterRequest();
    loginResponse = new LoginResponse();
    loginResponse.setUserId(userId);

    when(matrixUserRegisterTransformer.transform(contact)).thenReturn(registerRequest);
    when(userNameTransformer.transform(contact)).thenReturn(displayName);

    when(matrixClient.account()).thenReturn(accountMethods);
    when(accountMethods.register(registerRequest)).thenReturn(registerFuture);

    when(matrixClient.userId(userId)).thenReturn(userClient);
    when(userClient.profile()).thenReturn(profileMethods);
    when(profileMethods.setDisplayName(displayName)).thenReturn(displayFuture);
    mockLogger = new MockLogger(MatrixUserService.class);
  }

  @AfterEach
  void tearDown() {
    mockLogger.teardown();
  }

  @Test
  void getUser_Exists() {
    when(userRepository.findDistinctByContact(contact))
        .thenReturn(Optional.of(chatUser));
    ChatUser result = userService.getUser(contact);
    assertEquals(chatUser, result);
    // user not created.
    verify(userRepository, times(0)).save(any());
    verify(matrixClient, times(0)).account();
  }

  @Test
  void getUser_CreatesUser() {
    when(userRepository.findDistinctByContact(contact)).thenReturn(Optional.empty());
    when(registerFuture.join()).thenReturn(loginResponse);
    when(userRepository.save(any())).thenReturn(chatUser);
    ChatUser result = userService.getUser(contact);

    assertEquals(chatUser, result);
    verify(userRepository).save(eq(ChatUser.builder()
        .ownerId(userId)
        .contact(contact)
        .userType(ChatUserType.VIRTUAL_USER)
        .build()));
    verify(profileMethods).setDisplayName(displayName);
  }

  @Test
  void getUser_CreateUser_RegisterCancelled() {
    when(userRepository.findDistinctByContact(contact)).thenReturn(Optional.empty());
    CancellationException e = new CancellationException();
    when(registerFuture.join()).thenThrow(e);

    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> userService.getUser(contact));

    assertEquals("Failed to create virtual sms user: ", thrown.getMessage());
    assertEquals(e, thrown.getCause());
    verify(userRepository, times(0)).save(any());
    verify(profileMethods, times(0)).setDisplayName(any());
  }

  @Test
  void getUser_CreateUser_RegisterCompletionException() {
    when(userRepository.findDistinctByContact(contact)).thenReturn(Optional.empty());
    CompletionException e = new CompletionException(new Exception());
    when(registerFuture.join()).thenThrow(e);

    BadRequestException thrown = assertThrows(BadRequestException.class,
        () -> userService.getUser(contact));

    assertEquals("Failed to create virtual sms user: ", thrown.getMessage());
    assertEquals(e, thrown.getCause());
    verify(userRepository, times(0)).save(any());
    verify(profileMethods, times(0)).setDisplayName(any());
  }

  @Test
  void getUser_CreateUser_DisplayNameFailed() {
    when(userRepository.findDistinctByContact(contact)).thenReturn(Optional.empty());
    when(registerFuture.join()).thenReturn(loginResponse);
    when(userRepository.save(any())).thenReturn(chatUser);
    ChatUser result = userService.getUser(contact);

    assertEquals(chatUser, result);
    verify(userRepository).save(eq(ChatUser.builder()
        .ownerId(userId)
        .contact(contact)
        .userType(ChatUserType.VIRTUAL_USER)
        .build()));
    verify(profileMethods).setDisplayName(displayName);
  }

  @Test
  void renameUser_Cancelled() {
    CancellationException e = new CancellationException();
    when(displayFuture.join()).thenThrow(e);
    userService.renameUser(userId, displayName);
    // exception is caught
    mockLogger.verify(Level.ERROR, "Failed to set user display name", e);
  }

  @Test
  void renameUser_CompletionException() {
    CompletionException e = new CompletionException(new Exception());
    when(displayFuture.join()).thenThrow(e);
    userService.renameUser(userId, displayName);
    // exception is caught
    mockLogger.verify(Level.ERROR, "Failed to set user display name", e);
  }

  @Test
  void renameUser_Success() {
    when(displayFuture.join()).thenReturn(new EmptyResponse());
    userService.renameUser(userId, displayName);
    verify(profileMethods).setDisplayName(displayName);
  }
}
