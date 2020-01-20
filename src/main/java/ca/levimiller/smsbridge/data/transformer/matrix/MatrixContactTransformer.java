package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.db.NumberRegistryRepository;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.transformer.qualifiers.From;
import ca.levimiller.smsbridge.data.transformer.qualifiers.To;
import ca.levimiller.smsbridge.error.TransformationException;
import ca.levimiller.smsbridge.service.RoomService;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class MatrixContactTransformer {

  private final NumberRegistryRepository numberRegistryRepository;
  private final RoomService roomService;

  @Inject
  public MatrixContactTransformer(
      NumberRegistryRepository numberRegistryRepository,
      RoomService roomService) {
    this.numberRegistryRepository = numberRegistryRepository;
    this.roomService = roomService;
  }

  /**
   * Transforms the room id to a phone number.
   * @param roomId - room id.
   * @return - number associated with room.
   */
  @To
  public Contact transformTo(String roomId) {
    return roomService.getNumber(roomId);
  }

  /**
   * Transforms a matrix user id to a Contact with a phone number.
   *
   * @param sender - matrix user id
   * @return - phone number linked to matrix user.
   * @throws TransformationException if there is no linked number.
   */
  @From
  public Contact transformFrom(String sender) throws TransformationException {
    return numberRegistryRepository.findDistinctByOwnerId(sender)
        .map(NumberRegistration::getContact)
        .orElseThrow(() -> new TransformationException(
            "Message sender isn't linked to a number: " + sender));
  }
}
