package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.UserNameTransformer;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixUserRegisterTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.service.UserService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import io.github.ma1uta.matrix.client.model.auth.LoginResponse;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MatrixUserService implements UserService {

  private final ChatUserRepository userRepository;
  private final MatrixUserRegisterTransformer matrixUserRegisterTransformer;
  private final UserNameTransformer userNameTransformer;
  private final AppServiceClient matrixClient;

  @Inject
  public MatrixUserService(
      ChatUserRepository userRepository,
      MatrixUserRegisterTransformer matrixUserRegisterTransformer,
      UserNameTransformer userNameTransformer,
      AppServiceClient matrixClient) {
    this.userRepository = userRepository;
    this.matrixUserRegisterTransformer = matrixUserRegisterTransformer;
    this.userNameTransformer = userNameTransformer;
    this.matrixClient = matrixClient;
  }

  @Override
  public ChatUser getUser(@Valid @NotNull Contact smsContact) {
    return userRepository.findDistinctByContact(smsContact).orElseGet(
        () -> userRepository.save(ChatUser.builder()
            .ownerId(createUser(smsContact))
            .contact(smsContact)
            .userType(ChatUserType.VIRTUAL_USER)
            .build()));
  }

  @Override
  public void renameUser(String userId, String newName) {
    try {
      matrixClient.userId(userId)
          .profile()
          .setDisplayName(newName)
          .join();
    } catch (CompletionException | CancellationException e ) {
      log.error("Failed to set user display name", e);
    }
  }

  /**
   * Creates a matrix user.
   *
   * @param smsContact - user to create.
   * @return - created matrix user id
   */
  private String createUser(Contact smsContact) {
    RegisterRequest request = matrixUserRegisterTransformer.transform(smsContact);
    try {
      LoginResponse response = matrixClient.account().register(request).join();
      renameUser(response.getUserId(), userNameTransformer.transform(smsContact));
      return response.getUserId();
    } catch (CancellationException | CompletionException e) {
      throw new BadRequestException("Failed to create virtual sms user: ", e);
    }
  }
}
