package ca.levimiller.smsbridge.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Transaction;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionRepositoryIT extends AbstractDbIT<Transaction> {

  private final TransactionRepository transactionRepository;

  @Autowired
  TransactionRepositoryIT(TransactionRepository transactionRepository,
      Fixture<Transaction> transactionFixture) {
    super(transactionFixture, transactionRepository);
    this.transactionRepository = transactionRepository;
  }

  @Test
  void findDistinctByNumber() {
    Transaction transaction = saveNewEntity();

    Optional<Transaction> result = transactionRepository
        .findDistinctByTransactionId(transaction.getTransactionId());
    assertTrue(result.isPresent());
    assertEquals(transaction, result.orElse(null));
  }
}
