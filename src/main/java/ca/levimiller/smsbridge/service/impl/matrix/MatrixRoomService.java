package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.config.MatrixConfig;
import ca.levimiller.smsbridge.data.dto.matrix.room.CreateRoomDto;
import ca.levimiller.smsbridge.data.dto.matrix.room.RoomDto;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.service.RoomService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MatrixRoomService implements RoomService {

  private final MatrixConfig matrixConfig;
  private final MatrixRoomTransformer roomTransformer;
  private final RestTemplate restTemplate;

  @Inject
  public MatrixRoomService(
      MatrixConfig matrixConfig,
      MatrixRoomTransformer roomTransformer,
      @Qualifier("matrixTemplate") RestTemplate restTemplate) {
    this.matrixConfig = matrixConfig;
    this.roomTransformer = roomTransformer;
    this.restTemplate = restTemplate;
  }

  @Override
  public String getRoom(NumberRegistration chatNumber, Contact smsContact) {
    CreateRoomDto roomDto = roomTransformer.transform(chatNumber, smsContact);
    RoomDto room;
    try {
      room = restTemplate.getForObject("/directory/room/{room_alias}",
          RoomDto.class, getFullAlias(roomDto.getRoomAliasName()));
    } catch (HttpClientErrorException error) {
      if (error.getStatusCode() != HttpStatus.NOT_FOUND) {
        throw new RestClientException("Unable to get or create matrix room. Server error:", error);
      }
      // create room if not present
      room = restTemplate.postForObject("/createRoom", roomDto, RoomDto.class);
    }
    if (room == null) {
      throw new RestClientException("Unable to get or create matrix room. Null response.");
    }
    return room.getRoomId();
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
