package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.VirtualUser;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VirtualUserRepositoryIT extends AbstractDbIT<VirtualUser> {
  private final VirtualUserRepository userRepository;

  @Autowired
  VirtualUserRepositoryIT(
      Fixture<VirtualUser> userFixture,
      VirtualUserRepository userRepository) {
    super(userFixture, userRepository);
    this.userRepository = userRepository;
  }

  @Test
  void findDistinctByContact() {
    VirtualUser user = saveNewEntity();

    Optional<VirtualUser> result = userRepository.findDistinctByContact(user.getContact());
    assertTrue(result.isPresent());
    assertEquals(user, result.orElse(null));
  }
}