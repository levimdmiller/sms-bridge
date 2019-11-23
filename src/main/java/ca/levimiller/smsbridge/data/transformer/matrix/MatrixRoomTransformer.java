package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.config.MatrixConfig;
import ca.levimiller.smsbridge.data.dto.matrix.room.CreateRoomDto;
import ca.levimiller.smsbridge.data.dto.matrix.room.VisibilityType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.model.NumberRegistrationType;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
import java.util.Collections;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class MatrixRoomTransformer {
  private final MatrixConfig matrixConfig;
  private final RoomNameTransformer roomNameTransformer;

  @Inject
  protected MatrixRoomTransformer(
      MatrixConfig matrixConfig,
      RoomNameTransformer roomNameTransformer) {
    this.matrixConfig = matrixConfig;
    this.roomNameTransformer = roomNameTransformer;
  }

  /**
   * Transforms the contact numbers to a room.
   * @param chatNumber - chat service identifier
   * @param smsContact - sms contact
   * @return - create room dto
   */
  public CreateRoomDto transform(NumberRegistration chatNumber, Contact smsContact) {
    return CreateRoomDto.builder()
        .visibility(VisibilityType.PRIVATE)
        .roomAliasName(String.format("%s:%s",
            roomNameTransformer.transformEncoded(chatNumber, smsContact),
            matrixConfig.getDomain()))
        .name(roomNameTransformer.transformHumanReadable(chatNumber, smsContact))
        .topic("Sms Conversation")
        .invite(Collections.singletonList(chatNumber.getOwnerId()))
        .isDirect(NumberRegistrationType.USER.equals(chatNumber.getRegistrationType()))
        .build();
  }
}
