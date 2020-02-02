package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.ChatUser;
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
}
