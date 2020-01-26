package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Room;
import ca.levimiller.smsbridge.data.model.RoomUser;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomFixture implements Fixture<Room> {
  private final RandomUtil randomUtil;
  private final Fixture<RoomUser> roomUserFixture;

  @Autowired
  public RoomFixture(RandomUtil randomUtil,
      Fixture<RoomUser> roomUserFixture) {
    this.randomUtil = randomUtil;
    this.roomUserFixture = roomUserFixture;
  }

  @Override
  public Room create() {
    Room room = Room.builder()
        .roomId(randomUtil.getString(255))
        .roomUser(randomUtil.getList(roomUserFixture))
        .build();
    room.getRoomUser().forEach(roomUser -> roomUser.setRoom(room));
    return room;
  }
}
