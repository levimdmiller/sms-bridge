package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Location;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationFixture implements Fixture<Location> {
  private final RandomUtil randomUtil;

  @Autowired
  public LocationFixture(RandomUtil randomUtil) {
    this.randomUtil = randomUtil;
  }

  @Override
  public Location create() {
    return Location.builder()
        .city(randomUtil.getString(255))
        .state(randomUtil.getString(255))
        .country(randomUtil.getString(255))
        .zip(randomUtil.getString(6))
        .build();
  }
}
