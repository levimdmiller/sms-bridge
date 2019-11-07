package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Contact;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ContactRepositoryTest extends AbstractDbIT<Contact> {

  private final ContactRepository contactRepository;

  @Autowired
  ContactRepositoryTest(Fixture<Contact> fixture,
      ContactRepository repository) {
    super(fixture, repository);
    contactRepository = repository;
  }

  @Test
  void findDistinctByNumber() {
    Contact contact = saveNewEntity();

    Optional<Contact> result = contactRepository.findDistinctByNumber(contact.getNumber());
    assertTrue(result.isPresent());
    assertEquals(contact, result.orElse(null));
  }
}
