package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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

  @Test
  void existsByContactAndUserType_Exists() {
    ChatUser owner = saveNewEntity();

    boolean result = chatUserRepository.existsByContactAndUserType(
        owner.getContact(), owner.getUserType());
    assertTrue(result);
  }

  @Test
  void existsByContactAndUserType_NotExists() {
    ChatUser owner = fixture.create();
    owner.setUserType(ChatUserType.VIRTUAL_USER);
    repository.save(owner);

    boolean result = chatUserRepository.existsByContactAndUserType(
        owner.getContact(), ChatUserType.USER);
    assertFalse(result);
  }

  @ParameterizedTest
  @EnumSource(value = ChatUserType.class)
  void isVirtual(ChatUserType type) {
    ChatUser owner = fixture.create();
    owner.setUserType(type);
    repository.save(owner);

    boolean result = chatUserRepository.isVirtual(
        owner.getContact());
    // true for VIRTUAL_USER, false for all other types.
    assertEquals(type == ChatUserType.VIRTUAL_USER, result);
  }


  @Test
  void existsByOwnerIdAndUserType_Exists() {
    ChatUser owner = saveNewEntity();

    boolean result = chatUserRepository.existsByOwnerIdAndUserType(
        owner.getOwnerId(), owner.getUserType());
    assertTrue(result);
  }

  @Test
  void existsByOwnerIdAndUserType_NotExists() {
    ChatUser owner = fixture.create();
    owner.setUserType(ChatUserType.VIRTUAL_USER);
    repository.save(owner);

    boolean result = chatUserRepository.existsByOwnerIdAndUserType(
        owner.getOwnerId(), ChatUserType.USER);
    assertFalse(result);
  }
}
