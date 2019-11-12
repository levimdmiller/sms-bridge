package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NumberRegistryRepositoryIT extends AbstractDbIT<NumberRegistration> {
  private final NumberRegistryRepository numberRegistryRepository;

  @Autowired
  NumberRegistryRepositoryIT(
      Fixture<NumberRegistration> fixture,
      NumberRegistryRepository repository) {
    super(fixture, repository);
    numberRegistryRepository = repository;
  }

  @Test
  void findDistinctByContact() {
    NumberRegistration owner = saveNewEntity();

    Optional<NumberRegistration> result = numberRegistryRepository.findDistinctByContact(owner.getContact());
    assertTrue(result.isPresent());
    assertEquals(owner, result.orElse(null));
  }
}
