package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.auth.SecurityRole;
import ca.levimiller.smsbridge.data.model.auth.SecurityUser;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserFixture implements Fixture<SecurityUser> {
  private final RandomUtil randomUtil;

  @Autowired
  public SecurityUserFixture(RandomUtil randomUtil) {
    this.randomUtil = randomUtil;
  }

  @Override
  public SecurityUser create() {
    return SecurityUser.builder()
        .username(randomUtil.getString(255))
        .password(randomUtil.getString(255))
        .role(randomUtil.getEnum(SecurityRole.class))
        .build();
  }
}
