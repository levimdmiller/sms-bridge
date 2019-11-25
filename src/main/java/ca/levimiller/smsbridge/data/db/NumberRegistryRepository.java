package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRegistryRepository extends JpaRepository<NumberRegistration, Long>,
    QuerydslPredicateExecutor<NumberRegistration> {

  /**
   * Finds the number registration by the given contact.
   *
   * @param contact - contact to look up registration from
   * @return - number registration
   */
  Optional<NumberRegistration> findDistinctByContact(Contact contact);
}
