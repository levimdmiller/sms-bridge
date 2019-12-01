package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaFixture implements Fixture<Media> {

  private final RandomUtil randomUtil;

  @Autowired
  public MediaFixture(RandomUtil randomUtil) {
    this.randomUtil = randomUtil;
  }

  @Override
  public Media create() {
    return Media.builder()
        .contentType(randomUtil.getString(255))
        .url(randomUtil.getString(255))
        .build();
  }
}
