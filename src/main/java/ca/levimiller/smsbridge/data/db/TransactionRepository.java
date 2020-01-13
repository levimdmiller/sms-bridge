package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    QuerydslPredicateExecutor<Transaction> {

}
