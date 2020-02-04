package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long>,
    QuerydslPredicateExecutor<ChatUser> {

  /**
   * Finds the number registration by the given contact.
   *
   * @param contact - contact to look up registration from
   * @return - number registration
   */
  Optional<ChatUser> findDistinctByContact(Contact contact);

  /**
   * Finds the number registration by owner id.
   *
   * @param ownerId - owner id
   * @return - number registration
   */
  Optional<ChatUser> findDistinctByOwnerId(String ownerId);

  /**
   * Checks if the contact is linked to a chat user of the given type.
   * @param contact = contact to check.
   * @param userType = user type to match.
   * @return = true if ChatUser of given type matches contact.
   */
  boolean existsByContactAndUserType(Contact contact, ChatUserType userType);

  /**
   * Checks if the contact is a virtual user.
   * @param contact - contact to check.
   * @return - true if attached to virtual user.
   */
  default boolean isVirtual(Contact contact) {
    return existsByContactAndUserType(contact, ChatUserType.VIRTUAL_USER);
  }
}
