package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Room;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomRepositoryIT extends AbstractDbIT<Room> {
  private final RoomRepository roomRepository;

  @Autowired
  RoomRepositoryIT(
      Fixture<Room> roomFixture,
      RoomRepository roomRepository) {
    super(roomFixture, roomRepository);
    this.roomRepository = roomRepository;
  }

  @Test
  void findDistinctByRoomId() {
    Room room = saveNewEntity();

    Optional<Room> result = roomRepository.findDistinctByRoomId(room.getRoomId());
    assertTrue(result.isPresent());
    assertEquals(room, result.orElse(null));
  }
}