package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Media;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>,
    QuerydslPredicateExecutor<Media> {
  Optional<Media> findDistinctByUid(UUID uuid);
}
