package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.dto.matrix.room.CreateRoomDto;
import ca.levimiller.smsbridge.data.dto.matrix.room.RoomDto;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.service.RoomService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MatrixRoomService implements RoomService {
  private final MatrixRoomTransformer roomTransformer;
  private final RestTemplate restTemplate;

  @Inject
  public MatrixRoomService(
      MatrixRoomTransformer roomTransformer,
      @Qualifier("matrixTemplate") RestTemplate restTemplate) {
    this.roomTransformer = roomTransformer;
    this.restTemplate = restTemplate;
  }

  @Override
  public String getRoom(NumberRegistration chatNumber, Contact smsContact) {
    CreateRoomDto roomDto = roomTransformer.transform(chatNumber, smsContact);
    RoomDto room = restTemplate.getForObject("/directory/room/{room_alias}",
          RoomDto.class, roomDto.getRoomAliasName());
      // create room if not present
    if(room == null) {
      room = restTemplate.postForObject("/createRoom", roomDto, RoomDto.class);
    }
    if(room == null) {
      throw new RestClientException("Unable to get or create matrix room. Null response.");
    }
    return room.getRoomId();
  }
}
