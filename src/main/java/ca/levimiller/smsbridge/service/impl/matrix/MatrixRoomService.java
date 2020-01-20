package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.config.MatrixConfig;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.transformer.PhoneNumberTransformer;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.error.BadRequestException;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.util.MatrixUtil;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import io.github.ma1uta.matrix.client.model.room.RoomId;
import io.github.ma1uta.matrix.event.RoomCanonicalAlias;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.content.RoomCanonicalAliasContent;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
public class MatrixRoomService implements RoomService {

  private final MatrixConfig matrixConfig;
  private final MatrixRoomTransformer roomTransformer;
  private final PhoneNumberTransformer phoneNumberTransformer;
  private final AppServiceClient matrixClient;
  private final MatrixUtil matrixUtil;

  @Inject
  public MatrixRoomService(
      MatrixConfig matrixConfig,
      MatrixRoomTransformer roomTransformer,
      PhoneNumberTransformer phoneNumberTransformer,
      AppServiceClient matrixClient, MatrixUtil matrixUtil) {
    this.matrixConfig = matrixConfig;
    this.roomTransformer = roomTransformer;
    this.phoneNumberTransformer = phoneNumberTransformer;
    this.matrixClient = matrixClient;
    this.matrixUtil = matrixUtil;
  }

  @Override
  public String getRoom(NumberRegistration chatNumber, Contact smsContact) {
    CreateRoomRequest roomRequest = roomTransformer.transform(chatNumber, smsContact);

    RoomId room;
    try {
      room = matrixClient.room()
          .resolveAlias(getFullAlias(roomRequest.getRoomAliasName()))
          .join();
    } catch (CancellationException | CompletionException error) {
      if (!matrixUtil.causedBy(error, HttpStatus.NOT_FOUND)) {
        throw new RestClientException("Unable to get or create matrix room. Server error:", error);
      }
      // create room if not present
      room = matrixClient.room().create(roomRequest).join();
    }
    return room.getRoomId();
  }

  @Override
  public Contact getNumber(String roomId) {
    try {
      EventContent canonicalAlias = matrixClient.event()
          .eventContent(roomId, RoomCanonicalAlias.TYPE, "").join();
      if (!(canonicalAlias instanceof RoomCanonicalAliasContent)) {
        throw new BadRequestException("Incorrect event content for alias lookup + "
            + canonicalAlias);
      }
      RoomCanonicalAliasContent aliasContent = (RoomCanonicalAliasContent) canonicalAlias;
      return Contact.builder()
          .number(phoneNumberTransformer.transform(aliasContent.getAlias()))
          .build();
    } catch (CancellationException | CompletionException error) {
      throw new BadRequestException("Unable to resolve canonical alias for roomId: " + roomId);
    }
  }

  /**
   * Gets the full room alias from the base (required for the GET, but must not exist on the POST)
   * e.g., for sms-test returns #sms-test:domain.ca
   *
   * @param baseAlias - base room alias (no # or domain)
   * @return - fully qualified room alias (#base:domain.ca)
   */
  private String getFullAlias(String baseAlias) {
    return String.format("#%s:%s", baseAlias, matrixConfig.getServerName());
  }
}
