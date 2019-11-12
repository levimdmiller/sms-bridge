package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.NumberOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRegistryRepository extends JpaRepository<NumberOwner, Long>,
    QuerydslPredicateExecutor<NumberOwner> {
}
