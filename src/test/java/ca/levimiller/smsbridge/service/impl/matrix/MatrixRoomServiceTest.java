package ca.levimiller.smsbridge.service.impl.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.model.NumberRegistrationType;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.service.RoomService;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import io.github.ma1uta.matrix.client.model.room.RoomResolveResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class MatrixRoomServiceTest {
  @MockBean
  private MatrixRoomTransformer roomTransformer;
  @MockBean
  @Qualifier("matrixTemplate")
  private RestTemplate restTemplate;
  private final RoomService roomService;

  private NumberRegistration chatNumber;
  private Contact smsContact;
  private CreateRoomRequest createRoomRequest;
  private RoomResolveResponse roomResponse;

  @Autowired
  MatrixRoomServiceTest(RoomService roomService) {
    this.roomService = roomService;
  }


  @BeforeEach
  void setUp() {
    chatNumber = NumberRegistration.builder()
        .ownerId("ownerId")
        .registrationType(NumberRegistrationType.USER)
        .contact(Contact.builder()
            .number("+registrationNumber")
            .build())
        .build();
    smsContact = Contact.builder()
        .number("+smsNumber")
        .build();
    createRoomRequest = new CreateRoomRequest();
    createRoomRequest.setRoomAliasName("alias");
    roomResponse = new RoomResolveResponse();
    roomResponse.setRoomId("roomId");

    when(roomTransformer.transform(chatNumber, smsContact)).thenReturn(createRoomRequest);
  }


  @Test
  void getRoom_NumberExists() {
    when(restTemplate.getForObject(
        "/directory/room/{room_alias}", RoomResolveResponse.class, "#alias:domain.ca"))
        .thenReturn(roomResponse);
    String response = roomService.getRoom(chatNumber, smsContact);
    assertEquals("roomId", response);
    verify(restTemplate, times(0))
        .postForObject("/createRoom", roomResponse, RoomResolveResponse.class);
  }

  @Test
  void getRoom_NumberNotFound() {
    when(restTemplate.getForObject(
        "/directory/room/{room_alias}", RoomResolveResponse.class, "#alias:domain.ca"))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
    when(restTemplate.postForObject(eq("/createRoom"), any(), eq(RoomResolveResponse.class)))
        .thenReturn(roomResponse);

    String response = roomService.getRoom(chatNumber, smsContact);
    assertEquals("roomId", response);
  }

  @Test
  void getRoom_NumberNotFoundOrCreated() {
    when(restTemplate.getForObject(
        "/directory/room/{room_alias}", RoomResolveResponse.class, "#alias:domain.ca"))
        .thenThrow(HttpClientErrorException.NotFound.class);
    when(restTemplate.postForObject("/createRoom", roomResponse, RoomResolveResponse.class))
        .thenReturn(roomResponse);

    assertThrows(RestClientException.class, () -> roomService.getRoom(chatNumber, smsContact));
  }
}
