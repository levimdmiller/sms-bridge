package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>,
    QuerydslPredicateExecutor<Message> {
}
