package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.NumberOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

class NumberRegistryRepositoryIT extends AbstractDbIT<NumberOwner> {

  @Autowired
  NumberRegistryRepositoryIT(
      Fixture<NumberOwner> fixture,
      JpaRepository<NumberOwner, Long> repository) {
    super(fixture, repository);
  }
}
