package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.RoomUser;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomUserFixture implements Fixture<RoomUser> {
  private final RandomUtil randomUtil;

  @Autowired
  public RoomUserFixture(RandomUtil randomUtil) {
    this.randomUtil = randomUtil;
  }

  @Override
  public RoomUser create() {
    return RoomUser.builder()
        .userId(randomUtil.getString(255))
        .build();
  }
}
