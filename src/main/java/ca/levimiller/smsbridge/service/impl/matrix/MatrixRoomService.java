package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.dto.matrix.room.CreateRoomDto;
import ca.levimiller.smsbridge.data.dto.matrix.room.RoomDto;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.service.RoomService;
import java.net.URI;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    URI uri = UriComponentsBuilder.fromPath("/directory/room/{room_alias}")
        .buildAndExpand(roomDto.getRoomAliasName())
        .toUri();
    RoomDto room;
    try {
      room = restTemplate.getForObject(uri, RoomDto.class);
    } catch (NotFound e) {
      // create room if not present
      room = restTemplate.postForObject("/createRoom", roomDto, RoomDto.class);
    }
    if(room == null) {
      throw new RestClientException("Unable to get or create matrix room. Null response.");
    }
    return room.getRoomId();
  }
}
