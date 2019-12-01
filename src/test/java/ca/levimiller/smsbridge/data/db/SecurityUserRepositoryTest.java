package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.auth.SecurityUser;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SecurityUserRepositoryTest extends AbstractDbIT<SecurityUser> {

  private final SecurityUserRepository userRepository;

  @Autowired
  SecurityUserRepositoryTest(
      Fixture<SecurityUser> fixture,
      SecurityUserRepository userRepository) {
    super(fixture, userRepository);
    this.userRepository = userRepository;
  }

  @Test
  void findByUsername() {
    SecurityUser user = saveNewEntity();

    Optional<SecurityUser> result = userRepository.findByUsername(user.getUsername());
    assertTrue(result.isPresent());
    assertEquals(user, result.orElse(null));
  }
}
