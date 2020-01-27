package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.VirtualUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VirtualUserRepository extends JpaRepository<VirtualUser, Long>,
    QuerydslPredicateExecutor<VirtualUser> {

  Optional<VirtualUser> findDistinctByContact(Contact contact);
}
