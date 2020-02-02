package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.ChatUser;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChatUserRepositoryIT extends AbstractDbIT<ChatUser> {
  private final ChatUserRepository chatUserRepository;

  @Autowired
  ChatUserRepositoryIT(
      Fixture<ChatUser> fixture,
      ChatUserRepository repository) {
    super(fixture, repository);
    chatUserRepository = repository;
  }

  @Test
  void findDistinctByContact() {
    ChatUser owner = saveNewEntity();

    Optional<ChatUser> result = chatUserRepository.findDistinctByContact(
        owner.getContact());
    assertTrue(result.isPresent());
    assertEquals(owner, result.orElse(null));
  }

  @Test
  void findDistinctByOwnerId() {
    ChatUser owner = saveNewEntity();

    Optional<ChatUser> result = chatUserRepository.findDistinctByOwnerId(
        owner.getOwnerId());
    assertTrue(result.isPresent());
    assertEquals(owner, result.orElse(null));
  }
}
