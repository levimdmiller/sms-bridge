package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.auth.SecurityUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long>,
    QuerydslPredicateExecutor<SecurityUser> {

  /**
   * Looks up the user by their username.
   * @param username - user to look up
   * @return - user if they exist.
   */
  Optional<SecurityUser> findByUsername(String username);
}
