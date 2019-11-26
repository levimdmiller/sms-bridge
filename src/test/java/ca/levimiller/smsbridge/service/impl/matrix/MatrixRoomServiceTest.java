package ca.levimiller.smsbridge.service.impl.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.dto.matrix.room.CreateRoomDto;
import ca.levimiller.smsbridge.data.dto.matrix.room.RoomDto;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.model.NumberRegistrationType;
import ca.levimiller.smsbridge.data.transformer.matrix.MatrixRoomTransformer;
import ca.levimiller.smsbridge.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
  private CreateRoomDto createRoomDto;
  private RoomDto roomDto;

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
    createRoomDto = CreateRoomDto.builder()
        .roomAliasName("alias")
        .build();
    roomDto = new RoomDto("roomId", null);

    when(roomTransformer.transform(chatNumber, smsContact)).thenReturn(createRoomDto);
  }


  @Test
  void getRoom_NumberExists() {
    when(restTemplate.getForObject(
        "/directory/room/{room_alias}", RoomDto.class, "#alias:domain.ca"))
        .thenReturn(roomDto);
    String response = roomService.getRoom(chatNumber, smsContact);
    assertEquals("roomId", response);
    verify(restTemplate, times(0))
        .postForObject("/createRoom", roomDto, RoomDto.class);
  }

  @Test
  void getRoom_NumberNotFound() {
    when(restTemplate.postForObject(eq("/createRoom"), any(), eq(RoomDto.class)))
        .thenReturn(roomDto);

    String response = roomService.getRoom(chatNumber, smsContact);
    assertEquals("roomId", response);
  }

  @Test
  void getRoom_NumberNotFoundOrCreated() {
    when(restTemplate.getForObject(
        "/directory/room/{room_alias}", RoomDto.class, "#alias:domain.ca"))
        .thenReturn(null);
    when(restTemplate.postForObject("/createRoom", roomDto, RoomDto.class))
        .thenReturn(roomDto);

    assertThrows(RestClientException.class, () -> roomService.getRoom(chatNumber, smsContact));
  }
}
